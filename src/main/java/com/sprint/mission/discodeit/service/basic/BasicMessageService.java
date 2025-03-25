package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.RestExceptions;
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


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public MessageDTO create(CreateMessageParam createMessageParam) {
        validateMessageInput(createMessageParam);
        Message message = createMessageEntity(createMessageParam);
        messageRepository.save(message);
        return messageEntityToDTO(message);
    }

    @Override
    public MessageDTO find(UUID messageId) {
        Message message = findMessageById(messageId);
        return messageEntityToDTO(message);
    }

    @Override
    public List<MessageDTO> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(message -> messageEntityToDTO(message))
                .toList();
    }

    @Override
    public UUID update(UpdateMessageParam updateMessageParam) {
        Message message = findMessageById(updateMessageParam.id());
        message.updateMessageInfo(updateMessageParam.content());
        messageRepository.save(message);
        return message.getId();
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findMessageById(messageId);
        if(message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            message.getAttachmentIds().stream()
                    .forEach(id -> binaryContentRepository.deleteById(id));
        }
        messageRepository.deleteById(messageId);
    }

    private void validateMessageInput(CreateMessageParam createMessageParam) {
        if (createMessageParam.content() == null || createMessageParam.content().isBlank()) {
            throw RestExceptions.BAD_REQUEST;
        }
        if (userRepository.findById(createMessageParam.authorId()).isEmpty()) {
            throw RestExceptions.USER_NOT_FOUND;
        }
        if(channelRepository.findById(createMessageParam.channelId()).isEmpty()) {
            throw RestExceptions.CHANNEL_NOT_FOUND;
        }
    }

    private Message createMessageEntity(CreateMessageParam createMessageParam) {
        return Message.builder()
                .attachmentIds(createMessageParam.attachmentsId())
                .authorId(createMessageParam.authorId())
                .channelId(createMessageParam.channelId())
                .content(createMessageParam.content())
                .build();
    }

    private MessageDTO messageEntityToDTO(Message message) {
        return new MessageDTO(message.getId(), message.getCreatedAt(), message.getUpdatedAt(), message.getAttachmentIds(), message.getContent(), message.getChannelId(), message.getAuthorId());
    }

    private Message findMessageById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> RestExceptions.MESSAGE_NOT_FOUND);
    }
}
