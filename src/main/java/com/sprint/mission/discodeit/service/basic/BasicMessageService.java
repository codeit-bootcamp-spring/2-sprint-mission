package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  public MessageResponse create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentsCreateRequest) {
    log.info("메시지 생성 시도: channelId={}, authorId={}", messageCreateRequest.channelId(),
        messageCreateRequest.authorId());
    Channel channel = channelRepository.getReferenceById(messageCreateRequest.channelId());
    User author = userRepository.getReferenceById(messageCreateRequest.authorId());

    List<BinaryContent> attachments = List.of();
    if (attachmentsCreateRequest != null) {
      log.debug("메시지 첨부 파일 생성 처리");
      attachments = attachmentsCreateRequest.stream().map(binaryContentService::create)
          .toList();
      log.debug("메시지 첨부 파일 생성 완료: {}개", attachments.size());
    }

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);

    messageRepository.save(message);
    log.info("메시지 생성 성공: messageId={}, channelId={}", message.getId(),
        messageCreateRequest.channelId());

    return messageMapper.toResponse(message);
  }

  @Override
  public MessageResponse find(UUID messageId) {
    log.debug("메시지 조회 시도: messageId={}", messageId);
    return messageMapper.toResponse(messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.warn("메시지 조회 실패 - 메시지를 찾을 수 없음: messageId={}", messageId);
              return new MessageNotFoundException();
            }));
  }

  @Transactional(readOnly = true)
  public PageResponse<MessageResponse> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    log.debug("채널 메시지 목록 조회 시도: channelId={}, cursor={}", channelId, createAt);
    Slice<MessageResponse> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toResponse);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }
    log.debug("채널 메시지 목록 조회 성공: channelId={}, {}개 메시지 발견, nextCursor={}", channelId,
        slice.getContent().size(), nextCursor);

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Override
  public MessageResponse update(UUID messageId, MessageUpdateRequest request) {
    log.info("메시지 수정 시도: messageId={}", messageId);
    Message message = getMessage(messageId);
    message.update(request.newContent());
    log.info("메시지 수정 성공: messageId={}", messageId);
    return messageMapper.toResponse(message);
  }

  @Override
  public void delete(UUID messageId) {
    log.info("메시지 삭제 시도: messageId={}", messageId);
    Message message = getMessage(messageId);

    if (message.getAttachments().isEmpty()) {
      log.debug("메시지 삭제 처리 (첨부 파일 없음): messageId={}", messageId);
      messageRepository.deleteById(messageId);
    } else {
      log.debug("메시지 삭제 처리 (첨부 파일 포함): messageId={}", messageId);
      message.getAttachments().forEach(binaryContentService::delete);
      log.debug("메시지 첨부 파일 삭제 완료: messageId={}", messageId);
      messageRepository.delete(message);
    }
    log.info("메시지 삭제 성공: messageId={}", messageId);
  }

  private Message getMessage(UUID messageId) {
    log.debug("ID로 메시지 조회: messageId={}", messageId);
    return messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("ID로 메시지 조회 실패 - 메시지를 찾을 수 없음: messageId={}", messageId);
          return new MessageNotFoundException();
        });
  }

  private void validateChannelExistence(UUID channelId) {
    log.debug("채널 존재 여부 검증: channelId={}", channelId);
    if (!channelRepository.existsById(channelId)) {
      log.warn("채널 존재 여부 검증 실패 - 채널을 찾을 수 없음: channelId={}", channelId);
      throw new ChannelNotFoundException();
    }
  }
}