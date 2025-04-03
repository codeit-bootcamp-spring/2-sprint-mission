package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.controller.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity._BinaryContent;
import com.sprint.mission.discodeit.entity._Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public _Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID getChannelId = messageCreateRequest.getgetChannelId();
    UUID authorId = messageCreateRequest.getAuthorId();

    if (!channelRepository.existsById(getChannelId)) {
      throw new NoSuchElementException("Channel with id " + getChannelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          _BinaryContent binaryContent = new _BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          _BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();

    String content = messageCreateRequest.getContent();
    _Message message = new _Message(
        content,
        getChannelId,
        authorId,
        attachmentIds
    );
    return messageRepository.save(message);
  }

  @Override
  public _Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<_Message> findAllBygetChannelId(UUID getChannelId) {
    return messageRepository.findAllBygetChannelId(getChannelId).stream()
        .toList();
  }

  @Override
  public _Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.getNewContent();
    _Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  public void delete(UUID messageId) {
    _Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachmentIds()
        .forEach(binaryContentRepository::deleteById);

    messageRepository.deleteById(messageId);
  }
}
