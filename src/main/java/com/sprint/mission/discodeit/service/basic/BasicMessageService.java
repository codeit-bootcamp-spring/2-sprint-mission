package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.controller.PageResponse;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryRequestList) {
    log.debug("메시지 생성 시작: request={}, file={}", createRequest, binaryRequestList);
    UUID channelId = createRequest.channelId();
    UUID authorId = createRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException().notFoundWithId(channelId));
    User author = userRepository.findByIdWithProfileAndUserStatus(authorId)
        .orElseThrow(() -> new UserNotFoundException().notFoundWithId(authorId));

    List<BinaryContent> attachmentList = binaryRequestList.stream()
        .map(basicBinaryContentService::create).toList();

    Message message = new Message(createRequest.content(), channel, author, attachmentList);
    messageRepository.save(message);
    log.info("메시지 생성 완료: id={}", message.getId());
    return messageMapper.toDto(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException().notFoundWithId(messageId));
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<MessageDto> messageDtoSlice = messageRepository
        .findAllByChannelIdWithAuthor(
            channelId, Optional.ofNullable(cursor).orElse(Instant.now()), pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!messageDtoSlice.isEmpty()) {
      nextCursor = messageDtoSlice.getContent().get(messageDtoSlice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(messageDtoSlice, nextCursor);
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateRequest updateRequest) {
    log.debug("메시지 수정 시작: id={}, {}", id, updateRequest);
    Message message = messageRepository.findByIdWithAuthorAndAttachments(id)
        .orElseThrow(() -> new MessageNotFoundException().notFoundWithId(id));
    message.update(updateRequest.newContent());
    log.info("메시지 수정 완료: id={}", message.getId());
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    log.debug("메시지 삭제 시작: id={}", messageId);
    if (!messageRepository.existsById(messageId)) {
      throw new MessageNotFoundException().notFoundWithId(messageId);
    }
    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료: id={}", messageId);
  }
}
