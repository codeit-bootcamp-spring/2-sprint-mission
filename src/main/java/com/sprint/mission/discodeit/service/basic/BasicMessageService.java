package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.NotificationEventPublisher;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentAsyncService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final BinaryContentAsyncService binaryContentAsyncService;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.debug("메시지 생성 시작: request={}", messageCreateRequest);
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> UserNotFoundException.withId(authorId));

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
          binaryContentRepository.save(binaryContent);
          binaryContentAsyncService.uploadFile(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );

    messageRepository.save(message);
    log.info("메시지 생성 완료: id={}, channelId={}", message.getId(), channelId);

    // 알림 이벤트 발생
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);

    // 알림에 사용할 채널명
    String channelName;
    if (channel.getName() != null && !channel.getName().isBlank()) {
      // public 채널
      channelName = channel.getName();
    } else {
      // private 채널 → 참여자 이름 나열
      List<User> participants = readStatuses.stream()
              .map(ReadStatus::getUser)
              .distinct()
              .toList();

      List<String> usernames = participants.stream()
              .map(User::getUsername)
              .toList();

      if (usernames.size() <= 3) {
        channelName = String.join(", ", usernames);
      } else {
        // 앞에서 3명만 보여주고 나머지는 생략
        channelName = String.join(", ", usernames.subList(0, 3)) + " 외 " + (usernames.size() - 3) + "명";
      }
    }

    for (ReadStatus rs : readStatuses) {
      UUID receiverId = rs.getUser().getId();
      log.debug("알림 수신자={}, 작성자={}, 알림설정={}", receiverId, authorId, rs.isNotificationEnabled());

      if (rs.isNotificationEnabled() && !rs.getUser().getId().equals(authorId)) {
        notificationEventPublisher.publish(new NotificationEvent(
                rs.getUser().getId(),
                "새 메시지가 도착했어요",
                channelName + " 채널에 새 메시지가 등록되었습니다.",
                NotificationType.NEW_MESSAGE,
                channelId
        ));
      }
    }

    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 수정 시작: id={}, request={}", messageId, request);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));

    message.update(request.newContent());
    log.info("메시지 수정 완료: id={}, channelId={}", messageId, message.getChannel().getId());
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.debug("메시지 삭제 시작: id={}", messageId);
    if (!messageRepository.existsById(messageId)) {
      throw MessageNotFoundException.withId(messageId);
    }
    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료: id={}", messageId);
  }
}
