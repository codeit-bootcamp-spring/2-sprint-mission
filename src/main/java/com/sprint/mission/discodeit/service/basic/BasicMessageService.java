package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.MessageMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataMessageAttachmentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final SpringDataMessageAttachmentRepository messageAttachmentRepository;
  private final UserStatusRepository userStatusRepository;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
                        List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    List<BinaryContent> binaryContents = binaryContentCreateRequests.stream()
            .map(attachmentRequest -> {
              String fileName = attachmentRequest.fileName();
              String contentType = attachmentRequest.contentType();
              byte[] bytes = attachmentRequest.bytes();

              BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                      contentType, bytes);
              return binaryContentRepository.save(binaryContent);
            })
            .toList();

    Channel channel = channelRepository.findById(channelId).orElse(null);
    User user = userRepository.findById(authorId).orElse(null);

    String content = messageCreateRequest.content();
    Message message = new Message(
            content,
            channel,
            user
    );

    Message saveMessage = messageRepository.save(message);
    binaryContents
            .stream()
            .map(binaryContent -> {
              MessageAttachment messageAttachment = new MessageAttachment(saveMessage, binaryContent);
              return messageAttachmentRepository.save(messageAttachment);
            }).toList();

    return messageMapper.toDto(saveMessage);
  }

  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
            .map(messageMapper::toDto)
            .orElseThrow(
                    () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository
            .findAllByChannelId(channelId)
            .stream()
            .map(messageMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
            .orElseThrow(
                    () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.update(newContent);
    Message updateMassage = messageRepository.save(message);
    return messageMapper.toDto(updateMassage);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    messageRepository.deleteById(messageId);
  }
}
