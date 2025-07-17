package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.event.CreateNotificationEvent;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileReadException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.event.S3UploadEvent;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final ApplicationEventPublisher eventPublisher;


  @Override
  @Transactional
  public CreateMessageResult create(CreateMessageCommand createMessageCommand,
      List<MultipartFile> multipartFiles) {
    // 유저와 채널이 실제로 존재하는지 검증
    User user = findUserById(createMessageCommand.authorId());
    Channel channel = findChannelById(createMessageCommand.channelId());

    List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);
    if (!binaryContentList.isEmpty() && binaryContentList != null) {
      // multipartFiles 순서대로 binaryContentList를 만들었으므로 순서가 일치함 -> 인덱스로 뽑아서 각각 메타데이터 / 로컬로 저장
      for (int i = 0; i < multipartFiles.size(); i++) {
        BinaryContent binaryContent = binaryContentList.get(i);
        MultipartFile multipartFile = multipartFiles.get(i);

        binaryContentService.create(binaryContent);
        try {
          eventPublisher.publishEvent(
              new S3UploadEvent(binaryContent.getId(), multipartFile.getBytes()));
        } catch (IOException e) {
          log.error("Message create failed: multipartFile read failed (filename: {})",
              multipartFile.getOriginalFilename());
          throw new FileReadException(Map.of("contentType", multipartFile.getContentType(),
              "size", multipartFile.getSize(),
              "filename", multipartFile.getOriginalFilename()));
        }
      }
    }

    Message message = createMessageEntity(user, channel, createMessageCommand, binaryContentList);
    messageRepository.save(message);

    // 메시지 생성 -> 채널 내 NotificationEnable = true인 모든 사용자에게 알림 발행
    List<User> channelUserList = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
            channel.getId()).stream()
        .map(ReadStatus::getUser)
        .toList();

    for (User channelUser : channelUserList) {
      eventPublisher.publishEvent(CreateNotificationEvent.builder()
          .type(NotificationType.NEW_MESSAGE)
          .content(message.getContent())
          .title(channel.getType().equals(ChannelType.PUBLIC) ? user.getUsername() + " (# "
              + channel.getName() + ")" : user.getUsername())
          .receiverId(channelUser.getId())
          .targetId(channel.getId())
          .build());
    }
    return messageMapper.toCreateMessageResult(message);
  }

  @Override
  @Transactional(readOnly = true)
  public FindMessageResult find(UUID messageId) {
    Message message = findMessageById(messageId, "find");
    return messageMapper.toFindMessageResult(message);
  }

  // 첫 페이징인 경우 (cursor 존재 X)
  @Override
  @Transactional(readOnly = true)
  public Slice<FindMessageResult> findAllByChannelIdInitial(UUID channelId, int limit) {
    Pageable pageable = PageRequest.of(0, limit);
    Slice<Message> messages = messageRepository.findAllByChannelIdInitial(channelId, pageable);
    return messages.map(messageMapper::toFindMessageResult);
  }

  // cursor가 존재할 경우
  @Override
  @Transactional(readOnly = true)
  // 프론트엔드 웹소켓 기반 실시간 통신이라 그런지 조회 쿼리가 시간마다 반복적으로 호출됨 -> 캐싱을 통해 해결
  public Slice<FindMessageResult> findAllByChannelIdAfterCursor(UUID channelId, Instant cursor,
      int limit) {
    Pageable pageable = PageRequest.of(0, limit); // Cursor 페이징을 JPQL로 하기 위함
    Slice<Message> messages = messageRepository.findAllByChannelIdAfterCursor(channelId, cursor,
        pageable);
    return messages.map(messageMapper::toFindMessageResult);
  }

  @Override
  @Transactional
  @PreAuthorize("principal.userDto.id == @basicMessageService.find(#messageId).author().id()")
  public UpdateMessageResult update(@Param("messageId") UUID messageId,
      UpdateMessageCommand updateMessageCommand,
      List<MultipartFile> multipartFiles) {
    Message message = findMessageById(messageId, "update");
    message.updateMessageInfo(updateMessageCommand.newContent());
    replaceAttachments(message, multipartFiles);
    messageRepository.save(message);
    return messageMapper.toUpdateMessageResult(message);
  }


  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == @basicMessageService.find(#messageId).author().id()")
  public void delete(@Param("messageId") UUID messageId) {
    Message message = findMessageById(messageId, "delete");
    if (message.getAttachments() != null) {
      message.getAttachments()
          .forEach(
              attachment -> binaryContentService.delete(attachment.getId()));
    }
    messageRepository.deleteById(messageId);
  }

  // multipartFiles 있을 시 원래 있던 binaryContent 삭제 후 수정 내용으로 재생성
  private void replaceAttachments(Message message, List<MultipartFile> multipartFiles) {
    // 디버깅 했을 때, multipartFile이 없어도 filename ="", size=0으로 들어오는 것을 발견하여, 이 경우 처리 X
    // 들어온 multipartFile이 없다면, 처리가 안될 것이고, 기존에 있던 이미지 유지
    if (multipartFiles.get(0).getSize() == 0) {
      return;
    }

    message.getAttachments()
        .forEach(attachment -> {
          binaryContentService.delete(attachment.getId());
          binaryContentStorage.delete(attachment.getId());
        });

    List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);

    for (int i = 0; i < multipartFiles.size(); i++) {
      BinaryContent binaryContent = binaryContentList.get(i);
      MultipartFile multipartFile = multipartFiles.get(i);

      binaryContentService.create(binaryContent);
      try {
        eventPublisher.publishEvent(
            new S3UploadEvent(binaryContent.getId(), multipartFile.getBytes()));
      } catch (IOException e) {
        log.error("Message update failed: multipartFile read failed (filename: {})",
            multipartFile.getOriginalFilename());
        throw new FileReadException(Map.of("contentType", multipartFile.getContentType(),
            "size", multipartFile.getSize(),
            "filename", multipartFile.getOriginalFilename()));
      }
    }

    // 변경감지로 commit 시점에 message 테이블에 Update 쿼리 날아감
    // + message가 JoinTable을 관리하므로, commit 시점에 message_attachments 테이블에도 Insert 쿼리 날아감
    message.updateBinaryContent(binaryContentList);
  }

  private List<BinaryContent> createBinaryContentList(List<MultipartFile> multipartFiles) {
    if (multipartFiles == null || multipartFiles.isEmpty()
        || multipartFiles.get(0).getSize() == 0) {
      return Collections.emptyList();
    }
    return multipartFiles.stream()
        .map(multipartFile -> {
          return BinaryContent.builder()
              .contentType(multipartFile.getContentType())
              .size(multipartFile.getSize())
              .filename(multipartFile.getOriginalFilename())
              .build();
        })
        .collect(Collectors.toList());
  }

  private Message createMessageEntity(User user, Channel channel,
      CreateMessageCommand createMessageCommand, List<BinaryContent> binaryContentList) {
    return Message.builder()
        .author(user)
        .channel(channel)
        .content(createMessageCommand.content())
        .attachments(binaryContentList)
        .build();
  }


  private Message findMessageById(UUID id, String method) {
    return messageRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("Message {} failed: message not found (messageId: {})", method, id);
          return new MessageNotFoundException(Map.of("messageId", id, "method", method));
        });
  }

  private User findUserById(UUID userId) {
    return userRepository.findById(userId).orElseThrow(() -> {
      log.warn("Message create failed: user not found (userId: {})", userId);
      return new UserNotFoundException(Map.of("userId", userId));
    });
  }

  private Channel findChannelById(UUID channelId) {
    return channelRepository.findById(channelId).orElseThrow(() -> {
      log.warn("Message create failed: channel not found (channelId: : {})", channelId);
      return new ChannelNotFoundException(Map.of("channelId", channelId));
    });
  }

  private ReadStatus findReadStatusByIds(UUID userId, UUID channelId) {
    return readStatusRepository.findByUserIdAndChannelId(userId, channelId).orElseThrow(() -> {
      log.warn("Message create failed: readStatus not found (channelId: : {})", channelId);
      return new ReadStatusNotFoundException(Map.of("userId", userId, "channelId", channelId));
    });
  }
}
