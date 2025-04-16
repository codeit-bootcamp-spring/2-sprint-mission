package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID getChannelId = messageCreateRequest.getChannelId();
    UUID authorId = messageCreateRequest.authorId();

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

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentStorage.put(binaryContent.getId(), bytes);

          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channelRepository.findById(getChannelId).orElseThrow(),
        userRepository.findById(authorId).orElseThrow(),
        binaryContentRepository.findAllByIdIn(attachmentIds)
    );
    return messageRepository.save(message);
  }

  @Override
  @Transactional(readOnly = true)
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Message> findAllBygetChannelId(UUID getChannelId) {
    return messageRepository.findAllByChannelId(getChannelId).stream()
        .toList();
  }

  @Override
  @Transactional
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent().toString();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachments().stream()
        .map(binaryContent -> binaryContent.getId())
        .forEach(binaryContentRepository::deleteById);

    messageRepository.deleteById(messageId);
  }
}
