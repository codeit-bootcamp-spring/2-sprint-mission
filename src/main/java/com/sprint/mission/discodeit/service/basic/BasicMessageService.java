package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  public Message create(MessageCreateRequestDto dto) {
    Optional.ofNullable(userRepository.findById(dto.getAuthorId()))
        .orElseThrow(
            () -> new UserNotFoundException("User with id " + dto.getAuthorId() + " not found"));

    Optional.ofNullable(channelRepository.findById(dto.getChannelID()))
        .orElseThrow(() -> new ChannelNotFoundException(
            "Channel with id " + dto.getChannelID() + " not found"));

    Message message = new Message(dto.getContent(), dto.getChannelID(), dto.getAuthorId(),
        dto.getContents());
    return messageRepository.save(message);
  }

  public Message find(UUID messageId) {
    return messageRepository.findById(messageId);
  }

  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  public Message update(MessageUpdateRequestDto dto) {
    Map<UUID, Message> messageData = messageRepository.getMessageData();

    Message messageNullable = messageData.get(dto.getMessageId());
    Message message = Optional.ofNullable(messageNullable)
        .orElseThrow(() -> new MessageNotFoundException(
            "Message with id " + dto.getMessageId() + " not found"));

    return messageRepository.update(message, dto.getNewContent());
  }

  public void delete(UUID messageId) {
    Map<UUID, Message> messageData = messageRepository.getMessageData();
    if (!messageData.containsKey(messageId)) {
      throw new MessageNotFoundException("Message with id " + messageId + " not found");
    }

    messageRepository.delete(messageId);
    messageData.get(messageId).getAttachmentIds().stream()
        .filter(id -> binaryContentRepository.findById(id) != null)
        .forEach(binaryContentRepository::delete);
  }
}
