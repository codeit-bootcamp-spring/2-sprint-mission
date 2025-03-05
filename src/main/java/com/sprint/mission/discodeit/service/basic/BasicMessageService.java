package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private static volatile BasicMessageService instance;
    private final MessageRepository messageRepository;

    private BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public static BasicMessageService getInstance(MessageRepository messageRepository) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if (instance == null) {
                    instance = new BasicMessageService(messageRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void create(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message 객체가 null 입니다.");
        }
        messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findById(messageId);
        messageRepository.delete(message.getId());
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = findById(messageId);
        messageRepository.update(message.getId(), content);
        messageRepository.save(message);
    }
}
