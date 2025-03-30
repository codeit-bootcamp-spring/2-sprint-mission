package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResult create(MessageCreationRequest messageCreationRequest, List<UUID> attachmentsIds) {
        Message message = messageRepository.save(new Message(messageCreationRequest.context(), messageCreationRequest.chanelId(), messageCreationRequest.userId(), attachmentsIds));

        return MessageResult.fromEntity(message);
    }

    @Override
    public MessageResult findById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return MessageResult.fromEntity(message);
    }

    @Override
    public List<MessageResult> findAllByChannelId(UUID channelId) {
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
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));
        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);

        return MessageResult.fromEntity(savedMessage);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        for (UUID attachmentId : message.getAttachmentIds()) {
            binaryContentRepository.delete(attachmentId);
        }

        messageRepository.delete(id);
    }
}
