package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Override
  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "allMessages", allEntries = true),
      @CacheEvict(value = "allChannels", allEntries = true)
  }
  )
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
          binaryContentStorage.put(binaryContent.getId(), multipartFile.getBytes());
        } catch (IOException e) {
          logger.error("파일 읽기 실패: {}", multipartFile.getOriginalFilename(), e);
          throw RestExceptions.FILE_READ_ERROR;
        }
      }
    }

    Message message = createMessageEntity(user, channel, createMessageCommand, binaryContentList);
    messageRepository.save(message);

    return messageMapper.toCreateMessageResult(message);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "message", key = "#p0")
  public FindMessageResult find(UUID messageId) {
    Message message = findMessageById(messageId);
    return messageMapper.toFindMessageResult(message);
  }

  // 첫 페이징인 경우 (cursor 존재 X)
  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "allMessages", key = "#p0")
  public Slice<FindMessageResult> findAllByChannelIdInitial(UUID channelId, int limit) {
    Pageable pageable = PageRequest.of(0, 20);
    Slice<Message> messages = messageRepository.findAllByChannelIdInitial(channelId, pageable);
    return messages.map(messageMapper::toFindMessageResult);
  }

  // cursor가 존재할 경우
  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "allMessages", key = "#p0 + '_' + #p1")
  // 프론트엔드 웹소켓 기반 실시간 통신이라 그런지 조회 쿼리가 시간마다 반복적으로 호출됨 -> 캐싱을 통해 해결
  public Slice<FindMessageResult> findAllByChannelIdAfterCursor(UUID channelId, Instant cursor,
      int limit) {
    Pageable pageable = PageRequest.of(0, 20); // Cursor 페이징을 JPQL로 하기 위함
    Slice<Message> messages = messageRepository.findAllByChannelIdAfterCursor(channelId, cursor,
        pageable);
    return messages.map(messageMapper::toFindMessageResult);
  }

  @Override
  @Transactional
  @CachePut(value = "message", key = "#p0")
  @CacheEvict(value = "allMessages", allEntries = true)
  public UpdateMessageResult update(UUID id, UpdateMessageCommand updateMessageCommand,
      List<MultipartFile> multipartFiles) {
    Message message = findMessageById(id);
    message.updateMessageInfo(updateMessageCommand.newContent());
    replaceAttachments(message, multipartFiles);
    return messageMapper.toUpdateMessageResult(message);
  }


  @Override
  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "allMessages", allEntries = true),
      @CacheEvict(value = "message", key = "#p0")
  })
  public void delete(UUID messageId) {
    Message message = findMessageById(messageId);
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
        binaryContentStorage.put(binaryContent.getId(), multipartFile.getBytes());
      } catch (IOException e) {
        logger.error("파일 읽기 실패: {}", multipartFile.getOriginalFilename(), e);
        throw RestExceptions.FILE_READ_ERROR;
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


  private Message findMessageById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("메시지 찾기 실패: {}", id);
          return RestExceptions.MESSAGE_NOT_FOUND;
        });
  }

  private User findUserById(UUID userId) {
    return userRepository.findById(userId).orElseThrow(() -> {
      logger.error("메시지 생성 중 유저 찾기 실패: {}", userId);
      return RestExceptions.USER_NOT_FOUND;
    });
  }

  private Channel findChannelById(UUID channelId) {
    return channelRepository.findById(channelId).orElseThrow(() -> {
      logger.error("메시지 생성 중 채널 찾기 실패: {}", channelId);
      return RestExceptions.CHANNEL_NOT_FOUND;
    });
  }
}
