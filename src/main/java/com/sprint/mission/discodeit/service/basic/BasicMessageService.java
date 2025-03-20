package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Message.CreateMessageDto;
import com.sprint.mission.discodeit.DTO.Message.MessageDto;
import com.sprint.mission.discodeit.DTO.Message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageDto create(CreateMessageDto dto) {
        try{
            channelService.find(dto.channelId());
            userService.find(dto.authorId());
        } catch (NoSuchElementException e){
            throw e;
        }

        Message message = new Message(dto.content(), dto.channelId(), dto.authorId());
        if (dto.attachmentIds() != null) {
            for (UUID attachmentId : dto.attachmentIds()) {
                binaryContentRepository.findById(attachmentId).orElseThrow(()
                        -> new NoSuchElementException("Attachment not found: " +attachmentId));
                message.addAttachment(attachmentId);
            }
        }
        Message savedMessage = messageRepository.save(message);
        return mapToDto(savedMessage);
    }

    @Override
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new NoSuchElementException("Message not found with ID: " + messageId);
        }
        return mapToDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<MessageDto> findAllByAuthorId(UUID authorId) {
        return messageRepository.findAll().stream()
                .filter(message -> message.getAuthorId().equals(authorId))
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public MessageDto update(UpdateMessageDto dto) {
        Message existingMessage = messageRepository.findById(dto.messageId());
        existingMessage.update(dto.newContent());
        Message updatedMessage = messageRepository.save(existingMessage);

        return mapToDto(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        Message existingMessage = messageRepository.findById(messageId);
        binaryContentRepository.findAllByIds(existingMessage.getAttachmentIds())
                .forEach(binaryContent -> binaryContentRepository.deleteById(binaryContent.getId()));

        messageRepository.delete(messageId);
    }

    private MessageDto mapToDto(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds()
        );
    }
}
