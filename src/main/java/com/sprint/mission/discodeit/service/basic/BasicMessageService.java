package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = request.channelId();
    UUID authorId = request.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel not found with id " + request.channelId());
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author not found with id " + request.channelId());
    }

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          return binaryContent;
        }).toList();

    String content = request.content();
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("Author not found with id " + authorId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel not found with id " + channelId));
    Message message = new Message(content, channel, author);
    message.setAttachmentIds(attachments);

    messageRepository.save(message);

    // 메시지를 생성할 때 유저 온라인 상태 업데이트
    UserStatus userStatus = userStatusRepository.findByUserId(request.authorId())
        .orElseThrow(
            () -> new NoSuchElementException("User not found with id " + request.authorId()));
    userStatus.updateLastActiveAt(Instant.now());

    return message;
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel not found with id " + channelId);
    }
    return messageRepository.findAll().stream()
        .filter(msg -> msg.getChannel().getId().equals(channelId))
        .toList();
  }

  @Override
  public Message update(UUID id, MessageUpdateRequest request) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found"));
    message.update(request.newContent());

    messageRepository.save(message);

    return message;
  }

  @Override
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachments().forEach(binaryContent ->
        binaryContentRepository.delete(binaryContent.getId()));
    messageRepository.deleteById(messageId);
  }
}
