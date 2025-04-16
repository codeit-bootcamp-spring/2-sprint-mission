package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mepper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelService channelService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageDto createMessage(CreateMessageRequest request) {
    UUID channelId = request.channelId();
    UUID authorId = request.authorId();

    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("AuthorId:" + authorId + " not found"));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("ChannelId:" + channelId + " not found"));

    Message message = messageRepository.save(
        Message.builder()
            .author(author)
            .channel(channel)
            .content(request.content())
            .build()
    );
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
    Message message = findMessageOrThrow(messageId);
    message.updateContent(request.newContent());
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void deleteMessage(UUID messageId) {
    messageRepository.deleteById(messageId);
  }

  @Override
  public void validateMessageExists(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new IllegalArgumentException("MessageId:" + messageId + " not found");
    }
  }

  private Message findMessageOrThrow(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("MessageId:" + messageId + " not found"));
  }
}
