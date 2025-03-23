package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageMap;

    public JCFMessageRepository() {
        messageMap = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        this.messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : messageMap.values()) {
            if (message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }
        return messages;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public boolean existsById(UUID messageId) {
        return this.messageMap.containsKey(messageId);
    }

    @Override
    public void deleteById(UUID messageId) {
        this.messageMap.remove(messageId);
    }
}
