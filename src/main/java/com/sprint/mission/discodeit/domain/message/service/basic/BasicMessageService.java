package com.sprint.mission.discodeit.domain.message.service.basic;

import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.basic.BinaryContentCore;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.notification.event.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentCore binaryContentCore;
  private final MessageResultMapper messageResultMapper;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  @Override
  public MessageResult create(
      MessageCreateRequest messageCreateRequest,
      List<BinaryContentRequest> files
  ) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(Map.of()));
    User user = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new UserNotFoundException(Map.of()));

    List<BinaryContent> attachments = binaryContentCore.createBinaryContents(files);
    Message savedMessage = messageRepository.save(
        new Message(channel, user, messageCreateRequest.content(), attachments));

    publishMessageEvent(user.getId(), channel);

    return messageResultMapper.convertToMessageResult(savedMessage);
  }

  @Transactional(readOnly = true)
  @Override
  public MessageResult getById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new MessageNotFoundException(Map.of()));

    return messageResultMapper.convertToMessageResult(message);
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageResult> getAllByChannelId(
      UUID channelId,
      ChannelMessagePageRequest channelMessagePageRequest
  ) {
    Instant cursorCreatedAt = getInstant(channelMessagePageRequest);
    Pageable pageable = createPageable(channelMessagePageRequest);

    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthorDesc(channelId,
        cursorCreatedAt, pageable);

    return PageResponse.of(messages, messageResultMapper::convertToMessageResult,
        getNextCursor(messages));
  }

  @Transactional
  @Override
  public MessageResult updateContext(UUID id, String context) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new MessageNotFoundException(Map.of()));

    message.updateContext(context);
    Message savedMessage = messageRepository.save(message);

    return messageResultMapper.convertToMessageResult(savedMessage);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    validateMessageExist(messageId);

    messageRepository.deleteById(messageId);
  }

  private void publishMessageEvent(UUID userId, Channel channel) {
    NewMessageNotificationEvent newMessageNotificationEvent = new NewMessageNotificationEvent(
        userId, channel.getId(), getChannelName(channel));
    eventPublisher.publishEvent(newMessageNotificationEvent);
  }

  private String getChannelName(Channel channel) {
    if (channel.getName() == null) {
      return "프라이빗";
    }
    return channel.getName();
  }

  private Pageable createPageable(ChannelMessagePageRequest request) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    return PageRequest.of(request.pageNumber(), request.pageSize(), sort);
  }

  private Instant getInstant(ChannelMessagePageRequest channelMessagePageRequest) {
    if (channelMessagePageRequest.cursor() == null) {
      return Instant.now();
    }
    return channelMessagePageRequest.cursor();
  }

  private Instant getNextCursor(Slice<Message> messages) {
    if (messages.getContent().isEmpty()) {
      return null;
    }
    return messages.getContent().get(messages.getContent().size() - 1).getCreatedAt();
  }

  private void validateMessageExist(UUID messageId) {
    if (messageRepository.existsById(messageId)) {
      return;
    }
    throw new MessageNotFoundException(Map.of());
  }

}
