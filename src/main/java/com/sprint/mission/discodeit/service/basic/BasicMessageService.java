package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
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
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId);
        if (message != null) {
            message.updateContent(newContent);
            messageRepository.save(message);
        }
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
