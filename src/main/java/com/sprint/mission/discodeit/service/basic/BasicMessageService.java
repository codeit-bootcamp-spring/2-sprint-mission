package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelService channelService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto createMessage(CreateMessageRequest request) {
    log.info("Create message request: {}", request);
    UUID channelId = request.channelId();
    UUID authorId = request.authorId();

    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new UserNotFoundException(authorId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));

    Message message = messageRepository.save(
        Message.builder()
            .author(author)
            .channel(channel)
            .content(request.content())
            .build()
    );

    log.info("Message created successfully: {}", message);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void addAttachment(UUID messageId, BinaryContent attachment) {
    findMessageOrThrow(messageId).addAttachment(attachment);
  }

  @Override
  public MessageDto findMessageById(UUID messageId) {
    return messageMapper.toDto(findMessageOrThrow(messageId));
  }

  @Override
  public List<MessageDto> findMessagesByUserAndChannel(UUID authorId, UUID channelId) {
    userService.validateUserExists(authorId);
    channelService.validateChannelExists(channelId);
    return messageRepository.findAllByChannelIdAndAuthorId(channelId, authorId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    channelService.validateChannelExists(channelId);
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findMessagesByCursor(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<Message> messages;

    if (cursor == null) {
      messages = messageRepository.findByChannelId(channelId, pageable);
    } else {
      messages = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
          channelId, cursor, pageable);
    }

    Slice<MessageDto> dtoSlice = messages.map(messageMapper::toDto);

    return pageResponseMapper.fromSlice(dtoSlice, MessageDto::createdAt);
  }

  @Override
  public List<MessageDto> findAllByUserId(UUID authorId) {
    userService.validateUserExists(authorId);
    return messageRepository.findAllByAuthorId(authorId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public MessageDto updateMessage(UUID messageId, UpdateMessageRequest request) {
    log.info("Update message request: {}", request);
    Message message = findMessageOrThrow(messageId);
    message.updateContent(request.newContent());

    log.info("Message updated successfully: {}", message);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void deleteMessage(UUID messageId) {
    log.warn("Delete message request: {}", messageId);
    messageRepository.deleteById(messageId);
    log.info("Message deleted successfully: {}", messageId);
  }

  @Override
  public void validateMessageExists(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND, Map.of("messageId", messageId));
    }
  }

  private Message findMessageOrThrow(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND,
            Map.of("messageId", messageId)));
  }
}
