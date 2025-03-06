package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private static final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }

    @Override
    public boolean exists(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
