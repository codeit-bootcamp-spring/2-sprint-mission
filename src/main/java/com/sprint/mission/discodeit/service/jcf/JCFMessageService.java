package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> getAllMessages() {
        return data.values().stream().toList();
    }

    @Override
    public void updateMessageContent(UUID id, String content) {
        if(data.containsKey(id)) {
            data.get(id).updateContent(content);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
