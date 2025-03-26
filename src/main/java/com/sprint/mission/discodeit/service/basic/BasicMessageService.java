package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.common.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.response.MessageCreateResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
    public MessageCreateResponse create(MessageCreateRequest request) {
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel not found with id " + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("Author not found with id " + request.channelId());
        }

        Message message = new Message(request.content(), request.channelId(), request.authorId());
        message.setAttachmentIds(request.attachmentIds());

        messageRepository.save(message);

        return new MessageCreateResponse(message.getId(), message.getChannelId(), message.getAuthorId(), message.getContent(), message.getAttachmentIds());
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId);
        }
        return messageRepository.findAll().stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public MessageUpdateDto update(MessageUpdateDto request) {
        Message message = messageRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + request.id() + " not found"));
        message.update(request.content());

        if(!request.attachmentIds().isEmpty()){
            message.setAttachmentIds(request.attachmentIds());
        }
        messageRepository.save(message);

        return new MessageUpdateDto(message.getId(), message.getContent(), message.getAttachmentIds());
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.getAttachmentIds().forEach(binaryContentRepository::delete);
        messageRepository.deleteById(messageId);
    }
}
