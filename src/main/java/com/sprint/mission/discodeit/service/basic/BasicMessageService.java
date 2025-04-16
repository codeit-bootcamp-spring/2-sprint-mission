package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Transactional
  @Override
  public Message createMessage(CreateMessageRequest request) {
    UUID channelId = request.channelId();
    UUID authorId = request.authorId();

    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("AuthorId:" + authorId + " not found"));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("ChannelId:" + channelId + " not found"));

    return messageRepository.save(
        Message.builder()
            .author(author)
            .channel(channel)
            .content(request.content())
            .build()
    );
  }

  @Transactional
  @Override
  public void addAttachment(UUID messageId, BinaryContent attachment) {
    findMessageById(messageId).addAttachment(attachment);
  }

  @Override
  public Message findMessageById(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(
            "MessageId: " + messageId + "not found"));
  }

  @Override
  public List<Message> findMessagesByUserAndChannel(UUID authorId, UUID channelId) {
    userService.validateUserExists(authorId);
    channelService.validateChannelExists(channelId);
    return messageRepository.findAllByChannelIdAndAuthorId(channelId, authorId);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    channelService.validateChannelExists(channelId);
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  public List<Message> findAllByUserId(UUID authorId) {
    userService.validateUserExists(authorId);
    return messageRepository.findAllByAuthorId(authorId);
  }

  @Transactional
  @Override
  public Message updateMessage(UUID messageId, UpdateMessageRequest request) {
    Message message = findMessageById(messageId);
    message.updateContent(request.newContent());
    return message;
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
}
