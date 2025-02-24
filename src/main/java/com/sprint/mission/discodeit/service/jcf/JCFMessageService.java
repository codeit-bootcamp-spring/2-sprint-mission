package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<>();

    public JCFMessageService() {}

    @Override
    public void create(Message message) {
        messages.put(message.getId(), message);
        System.out.println("[메시지 ID]" + message.getId());
    }

    @Override
    public Message find(UUID id) {
        return messages.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void update(UUID id, Message message) {
        Message old = messages.get(id);
        old.updateContent(message.getContent());
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
