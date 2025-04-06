package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
    private final ChannelRepository channelRepository;

    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest,
                                List<BinaryContentRequest> files) {

        Channel channel = channelRepository.findByChannelId(messageCreateRequest.channelId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널이 존재하지 않습니다."));

        List<UUID> attachmentsIds;
        if (files != null) {
            attachmentsIds = files.stream()
                    .map(binaryContentRequest -> {
                        BinaryContent binaryContent = new BinaryContent(
                                binaryContentRequest.fileName(),
                                binaryContentRequest.contentType(),
                                binaryContentRequest.bytes());

                        return binaryContentRepository.save(binaryContent);
                    })
                    .map(BinaryContent::getId)
                    .toList();
        } else {
            attachmentsIds = List.of();
        }

        Message message = messageRepository.save(
                new Message(messageCreateRequest.content(), channel.getId(),
                        messageCreateRequest.authorId(), attachmentsIds));

        return MessageResult.fromEntity(message);
    }

    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findByMessageId(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

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
        Message message = messageRepository.findByMessageId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);

        return MessageResult.fromEntity(savedMessage);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findByMessageId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        for (UUID attachmentId : message.getAttachmentIds()) {
            binaryContentRepository.delete(attachmentId);
        }

        messageRepository.delete(id);
    }
}
