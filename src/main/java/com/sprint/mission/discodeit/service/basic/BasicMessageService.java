package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String message, UUID userId, UUID channelId) {
        Message newMessage = new Message(message, userId, channelId);
        return messageRepository.save(newMessage);
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        return messageRepository.findByUser(userId);
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        return messageRepository.findByChannel(channelId);
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        return  messageRepository.findByUserAndByChannel(userId, channelId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        return messageRepository.update(messageId, newMessage);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
