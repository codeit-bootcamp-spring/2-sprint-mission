package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageData;

    public JCFMessageRepository() {
        this.messageData = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        messageData.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageData.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messageData.containsKey(messageId);
    }

    @Override
    public void delete(UUID messageId) {
        this.messageData.remove(messageId);
    }
}
