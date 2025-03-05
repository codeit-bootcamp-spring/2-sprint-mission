package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageStorage = new HashMap<>();

    @Override
    public void save(Message message) {
        messageStorage.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return messageStorage.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageStorage.values());
    }

    public void delete(UUID id) {
        messageStorage.remove(id);
    }
}
