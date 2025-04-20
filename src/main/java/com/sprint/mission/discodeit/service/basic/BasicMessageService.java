package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest,
                                List<BinaryContentRequest> files) {

        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 채널이 존재하지 않습니다."));

        List<BinaryContent> attachments;
        if (files != null) {
            attachments = files.stream()
                    .map(binaryContentRequest -> {
                        BinaryContent binaryContent = new BinaryContent(
                                binaryContentRequest.fileName(),
                                binaryContentRequest.contentType(),
                                binaryContentRequest.bytes());

                        return binaryContentRepository.save(binaryContent);
                    })
                    .toList();
        } else {
            attachments = List.of();
        }

        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        Message message = messageRepository.save(
                new Message(channel, user, messageCreateRequest.content(), attachments));

        return MessageResult.fromEntity(message);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return MessageResult.fromEntity(message);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageResult> getAllByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageResult::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public MessageResult updateContext(UUID id, String context) {
        Message message = messageRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        message.updateContext(context);

        return MessageResult.fromEntity(message);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));


        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        for (UUID attachmentId : attachmentIds) {
            binaryContentRepository.deleteById(attachmentId);
        }

        messageRepository.deleteById(id);
    }
}
