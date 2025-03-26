package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

//@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("Author not found with id " + authorId);
        }

        Message message = new Message(UUID.randomUUID(), channelId, content);
        return messageRepository.save(message);
    }

    @Override
    public Message createMessage(MessageCreateRequestDTO dto) {
        if (!channelRepository.existsById(dto.getChannelId())) {
            throw new NoSuchElementException("Channel not found with id " + dto.getChannelId());
        }
        if (!userRepository.existsById(dto.getAuthorId())) {
            throw new NoSuchElementException("Author not found with id " + dto.getAuthorId());
        }

        Message message = new Message(UUID.randomUUID(), dto.getChannelId(), dto.getContent());
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> find(UUID messageId) {
        return Optional.ofNullable(messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found")));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message updateMessage(MessageUpdateRequestDTO dto) {
        Message message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + dto.getMessageId()));

        message.setContent(dto.getNewContent());
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.setContent(newContent);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.setContent(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteById(messageId);
    }
}