package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public void create(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public void update(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }

    @Override
    public Message find(UUID id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }
}
