package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.PageResponse;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.event.NewMessageEvent;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.storage.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.storage.repository.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final ApplicationEventPublisher eventPublisher;

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

    Map<UUID, byte[]> bytesMap = new HashMap<>();
    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          UUID binaryContentId = binaryContent.getId();
          bytesMap.put(binaryContentId, bytes);
          return binaryContent;
        })
        .toList();

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            attachments.forEach(binaryContent -> {
              UUID binaryContentId = binaryContent.getId();
              binaryContentStorage.putAsync(binaryContentId, bytesMap.get(binaryContentId))
                  .thenAccept(result -> {
                    log.debug("메시지에 포함된 첨부파일 업로드 성공: {}", binaryContentId);
                    binaryContentRepository.updateUploadStatus(binaryContentId,
                        BinaryContentUploadStatus.SUCCESS);
                  })
                  .exceptionally(ex -> {
                    log.error("메시지에 포함된 첨부파일 업로드 실패: {}", binaryContentId, ex);
                    binaryContentRepository.updateUploadStatus(binaryContentId,
                        BinaryContentUploadStatus.FAILED);
                    return null;
                  })
              ;
            });
          }
        });

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );

    messageRepository.save(message);
    log.info("메시지 생성 완료: id={}, channelId={}", message.getId(), channelId);

    MessageDto messageDto = MessageDto.from(message);
    eventPublisher.publishEvent(new NewMessageEvent(messageDto));
    return messageDto;
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(MessageDto::from)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(MessageDto::from);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return PageResponse.of(slice, nextCursor);
  }

  @PreAuthorize("principal.userDto.id == @basicMessageService.find(#messageId).author.id")
  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 수정 시작: id={}, request={}", messageId, request);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));

    message.update(request.newContent());
    log.info("메시지 수정 완료: id={}, channelId={}", messageId, message.getChannel().getId());
    return MessageDto.from(message);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == @basicMessageService.find(#messageId).author.id")
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
