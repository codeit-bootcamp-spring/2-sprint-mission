package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private static volatile BasicMessageService instance;
    private final MessageRepository messageRepository;

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

    private BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = find(messageId);
        message.update(newContent);
        messageRepository.save(message);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        find(messageId);
        messageRepository.deleteById(messageId);
    }
}
