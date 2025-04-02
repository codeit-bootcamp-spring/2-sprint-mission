package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ChannelRepository channelRepository;

  @Override
  public MessageResult create(MessageCreationRequest messageCreationRequest,
      List<MultipartFile> files) {

    Channel channel = channelRepository.findById(messageCreationRequest.channelId())
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널이 존재하지 않습니다."));

    List<UUID> attachmentsIds = files.stream()
        .map(file -> binaryContentRepository.save(new BinaryContent(file)))
        .map(BinaryContent::getId)
        .toList();

    Message message = messageRepository.save(
        new Message(messageCreationRequest.context(), channel.getId(),
            messageCreationRequest.userId(), attachmentsIds));

    return MessageResult.fromEntity(message);
  }

  @Override
  public MessageResult getById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

    return MessageResult.fromEntity(message);
  }

  @Override
  public List<MessageResult> getAllByChannelId(UUID channelId) {
    return messageRepository.findAll()
        .stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .sorted(Comparator.comparing(Message::getCreatedAt))
        .map(MessageResult::fromEntity)
        .toList();
  }

  @Override
  public MessageResult updateContext(UUID id, String context) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

    message.updateContext(context);
    Message savedMessage = messageRepository.save(message);

    return MessageResult.fromEntity(savedMessage);
  }

  @Override
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

    for (UUID attachmentId : message.getAttachmentIds()) {
      binaryContentRepository.delete(attachmentId);
    }

    messageRepository.delete(id);
  }
}
