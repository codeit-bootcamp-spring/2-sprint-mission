package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));

    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new NoSuchElementException("Author not found"));

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(req -> binaryContentRepository.save(
            new BinaryContent(req.fileName(), (long) req.bytes().length, req.contentType(),
                req.bytes())
        ))
        .toList();

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));
    return messageRepository.findAllByChannel(channel);
  }

  @Override
  @Transactional
  public Message update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found"));

    message.update(request.newContent());

    return message;
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found"));

    message.getAttachments().forEach(binaryContentRepository::delete);
    messageRepository.delete(message);
  }
}
