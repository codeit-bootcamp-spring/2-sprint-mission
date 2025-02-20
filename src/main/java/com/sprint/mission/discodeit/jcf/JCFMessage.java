package com.sprint.mission.discodeit.jcf;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessage implements MessageService {
    HashMap<UUID, Message> messages = new HashMap<>();

    @Override
    public void create(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void update(UUID id) {
        Optional<Message> message = this.findById(id);

        if (message.isEmpty()) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }

        message.ifPresent(ch -> ch.setUpdatedAt(System.currentTimeMillis()));
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
