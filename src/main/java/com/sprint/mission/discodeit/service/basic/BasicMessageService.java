package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelService channelService;
  private final UserService userService;
  private final BinaryContentRepository binaryContentRepository;

  private void saveMessageData() {
    messageRepository.save();
  }

  @Override
  public Message createMessage(CreateMessageRequest request) {
    UUID channelId = request.getChannelId();
    UUID authorId = request.getAuthorId();
    Message message = new Message(authorId, channelId, request.getContent());

//        for (String path : request.getFilePath()) {
//            BinaryContent binaryContent = new BinaryContent(path);
//            binaryContentRepository.addBinaryContent(binaryContent);
//            message.addAttachment(binaryContent.id());
//        }

    messageRepository.addMessage(message);
    return message;
  }

  @Override
  public Message getMessageById(UUID messageId) {
    validateMessageExists(messageId);
    return messageRepository.findMessageById(messageId);
  }

  @Override
  public List<BinaryContent> findAttachmentsById(UUID messageId) {
    validateMessageExists(messageId);
    List<BinaryContent> binaryContents = new ArrayList<>();
    messageRepository.findMessageById(messageId).getAttachmentIds().forEach(attachmentId -> {
      binaryContents.add(binaryContentRepository.findBinaryContentById(attachmentId));
    });

    return binaryContents;
  }

  @Override
  public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
    userService.validateUserExists(senderId);
    channelService.validateChannelExists(channelId);
    return messageRepository.findMessageAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .filter(message -> message.getAuthorId().equals(senderId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Message> findallByChannelId(UUID channelId) {
    channelService.validateChannelExists(channelId);
    return messageRepository.findMessageAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Message> findallByUserId(UUID senderId) {
    userService.validateUserExists(senderId);
    return messageRepository.findMessageAll().stream()
        .filter(message -> message.getAuthorId().equals(senderId))
        .collect(Collectors.toList());
  }

  @Override
  public void updateMessage(UUID userId, UUID messageId, UpdateMessageRequest request) {
    Message message = messageRepository.findMessageById(messageId);
    if (!message.getAuthorId().equals(userId)) {
      throw new RuntimeException("본인의 메시지만 수정할 수 있습니다.");
    }
    message.updateContent(request.getContent());
    saveMessageData();
  }

  @Override
  public void deleteMessage(UUID userId, UUID messageId) {
    Message message = messageRepository.findMessageById(messageId);

    if (!message.getAuthorId().equals(userId)) {
      throw new RuntimeException("본인의 메시지만 수정할 수 있습니다.");
    }

    binaryContentRepository.deleteBinaryContentById(messageId);

    messageRepository.deleteMessageById(messageId);
  }

  @Override
  public void validateMessageExists(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
    }
  }
}
