package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import 생략된 부분 포함
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.message.ChannelNotFoundForMessageException;
import com.sprint.mission.discodeit.exception.message.AuthorNotFoundForMessageException;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.debug("메시지 생성 요청: channelId={}, authorId={}", messageCreateRequest.channelId(),
        messageCreateRequest.authorId());

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> {
          log.warn("메시지 생성 실패 - 채널 없음: {}", messageCreateRequest.channelId());
          return new ChannelNotFoundForMessageException(messageCreateRequest.channelId());
        });

    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> {
          log.warn("메시지 생성 실패 - 작성자 없음: {}", messageCreateRequest.authorId());
          return new AuthorNotFoundForMessageException(messageCreateRequest.authorId());
        });

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(req -> {
          log.debug("첨부파일 저장: {}", req.fileName());
          BinaryContent binary = new BinaryContent(req.fileName(), (long) req.bytes().length,
              req.contentType());
          binaryContentRepository.save(binary);
          binaryContentStorage.put(binary.getId(), req.bytes());
          return binary;
        }).toList();

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);
    messageRepository.save(message);
    log.info("메시지 생성 완료: id={}", message.getId());

    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    log.debug("메시지 조회 요청: id={}", messageId);
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> {
          log.warn("메시지 조회 실패 - 존재하지 않음: id={}", messageId);
          return new MessageNotFoundException(messageId);
        });
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    log.debug("채널 메시지 목록 조회 요청: channelId={}", channelId);
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(
            channelId, Optional.ofNullable(createAt).orElse(Instant.now()), pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = slice.hasContent()
        ? slice.getContent().get(slice.getContent().size() - 1).createdAt()
        : null;

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메시지 수정 요청: id={}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("메시지 수정 실패 - 존재하지 않음: id={}", messageId);
          return new MessageNotFoundException(messageId);
        });

    message.update(request.newContent());
    log.info("메시지 수정 완료: id={}", messageId);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.debug("메시지 삭제 요청: id={}", messageId);
    if (!messageRepository.existsById(messageId)) {
      log.warn("삭제 실패 - 메시지 없음: id={}", messageId);
      throw new MessageNotFoundException(messageId);
    }

    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료: id={}", messageId);
  }
}

