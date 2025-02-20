package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService(Map<UUID, Message> data) {
        this.data = new HashMap<>();
    }

    @Override
    public Message createMessage(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message readMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAllMessages() {
        return List.of();
    }

    @Override
    public Message updateMessage(UUID id, Message message) {
        if(data.containsKey(id)) {
            Message existingMessage = data.get(id);
            existingMessage.update(message.getMessage());
            return existingMessage;
        }
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
