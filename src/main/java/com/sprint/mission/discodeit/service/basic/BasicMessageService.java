package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentService binaryContentService;

  @Override
  public Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentsCreateRequest) {
    Channel channel = channelRepository.getReferenceById(messageCreateRequest.channelId());
    User author = userRepository.getReferenceById(messageCreateRequest.authorId());

    List<BinaryContent> attachments = List.of();
    if (attachmentsCreateRequest != null) {
      attachments = attachmentsCreateRequest.stream().map(binaryContentService::create)
          .toList();
    }

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);

    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Message with id " + messageId + " not found"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    validateChannelExistence(channelId);
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    Message message = getMessage(messageId);
    message.update(request.newContent());
    return messageRepository.save(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = getMessage(messageId);

    if (message.getAttachments().isEmpty()) {
      messageRepository.deleteById(messageId);
      return;
    }

    message.getAttachments()
        .forEach(attachment -> {
          if (binaryContentRepository.existsById(attachment.getId())) {
            binaryContentRepository.delete(attachment);
          }
        });
    messageRepository.delete(message);
  }

  private Message getMessage(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 메세지 없음"));
  }

  private void validateUserExistence(UUID authorId) {
    if (!userRepository.existsById(authorId)) {
      throw new ResourceNotFoundException("해당 유저 없음");
    }
  }

  private void validateChannelExistence(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new ResourceNotFoundException("해당 채널 없음");
    }
  }
}
