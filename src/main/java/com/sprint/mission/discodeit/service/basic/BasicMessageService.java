package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.event.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;

  private final ApplicationEventPublisher publisher;
  private final CacheManager cacheManager;

  @Transactional
  @Override
  @CacheEvict(value="messages", key="#p0.channelId() + '_'")
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

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, BinaryContentUploadStatus.WAITING);

          binaryContentRepository.save(binaryContent);

          publisher.publishEvent(new BinaryContentUploadEvent(binaryContent.getId(), bytes));

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

    publisher.publishEvent(new MessageCreatedEvent(channelId, message.getContent()));

    log.info("메시지 생성 완료: id={}, channelId={}", message.getId(), channelId);
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
  @Cacheable(value="messages", key="#p0 + '_'")
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt, Pageable pageable) {
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

  @PreAuthorize("principal.userDto.id == @basicMessageService.find(#messageId).author.id")
  @CacheEvict(value = "messages", allEntries = true)
  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 수정 시작: id={}, request={}", messageId, request);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));

    message.update(request.newContent());

    MessageDto messageDto = messageMapper.toDto(message);

    log.info("메시지 수정 완료: id={}, channelId={}", messageId, message.getChannel().getId());
    return messageDto;
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == @basicMessageService.find(#messageId).author.id")
  @CacheEvict(value = "messages", allEntries = true)
  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.debug("메시지 삭제 시작: id={}", messageId);
    if (!messageRepository.existsById(messageId)) {
      throw MessageNotFoundException.withId(messageId);
    }

    Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> MessageNotFoundException.withId(messageId));

    messageRepository.deleteById(messageId);

    log.info("메시지 삭제 완료: id={}", messageId);
  }
}
