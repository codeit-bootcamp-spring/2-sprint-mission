package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final HashMap<UUID, Message> messages = new HashMap<>();

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public Optional<List<Message>> findAll() {
        return Optional.of(new ArrayList<>(messages.values()));
    }

    @Override
    public void update(Message message) {
        save(message);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
