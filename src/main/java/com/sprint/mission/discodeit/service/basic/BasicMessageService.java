package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
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

  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Optional.ofNullable(userRepository.findById(authorId))
        .orElseThrow(
            () -> new UserNotFoundException("User with id " + authorId + " not found"));

    Optional.ofNullable(channelRepository.findById(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(
            "Channel with id " + channelId + " not found"));

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();

    Message message = new Message(messageCreateRequest.content(), channelId, authorId,
        attachmentIds);
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

  public Message update(UUID messageId, MessageUpdateRequest request) {
    Map<UUID, Message> messageData = messageRepository.getMessageData();

    Message messageNullable = messageData.get(messageId);
    Message message = Optional.ofNullable(messageNullable)
        .orElseThrow(() -> new MessageNotFoundException(
            "Message with id " + messageId + " not found"));

    return messageRepository.update(message, request.newContent());
  }

  public void delete(UUID messageId) {
    Map<UUID, Message> messageData = messageRepository.getMessageData();
    if (!messageData.containsKey(messageId)) {
      throw new MessageNotFoundException("Message with id " + messageId + " not found");
    }

    messageRepository.delete(messageId);
    messageData.get(messageId).getAttachmentIds().stream()
        .filter(id -> binaryContentRepository.findById(id).isPresent())
        .forEach(binaryContentRepository::deleteById);
  }
}
