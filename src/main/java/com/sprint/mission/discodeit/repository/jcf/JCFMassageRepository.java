package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMassageRepository implements MessageRepository {
    private final Map<UUID, Message> messageMap;

    public JCFMassageRepository() {
        messageMap = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        this.messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public Message update(Message message) {
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public boolean delete(UUID messageId) {
        return messageMap.remove(messageId) != null;
    }
}