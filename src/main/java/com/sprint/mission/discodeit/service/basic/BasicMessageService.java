package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    String content = messageCreateRequest.content();

    checkUserExists(authorId);
    checkChannelExists(channelId);

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .toList();

    Message message = new Message(
        authorId,
        channelId,
        content,
        attachmentIds
    );
    return messageRepository.save(message);
  }

  @Override
  public Message findById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  public Message updateMessage(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + messageId));

    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + messageId));
    checkUserExists(message.getAuthorId());
    ensureMessageBelongsToChannel(message, message.getChannelId());

    if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
      message.getAttachmentIds()
          .forEach(binaryContentRepository::deleteById);
    }

    messageRepository.deleteById(messageId);
  }


  /*******************************
   * Validation check
   *******************************/
  private void ensureMessageBelongsToChannel(Message message, UUID channelId) {
    if (!message.getChannelId().equals(channelId)) {
      throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
    }
  }

  private void checkUserExists(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
    }
  }

  private void checkChannelExists(UUID channelId) {
    if (channelRepository.findById(channelId).isEmpty()) {
      throw new NoSuchElementException("해당 채널이 존재하지 않습니다. : " + channelId);
    }
  }


}
