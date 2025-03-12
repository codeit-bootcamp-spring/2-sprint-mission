package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getUuid(), message);
        return data.get(message.getUuid());
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }

    @Override
    public Message findByKey(UUID messageKey) {
        return data.get(messageKey);
    }

    @Override
    public Message findByChannelKey(UUID channelKey) {
        return data.values().stream()
                .filter(m -> m.getChannelKey().equals(channelKey))
                .findFirst()
                .orElse(null);
    }
    @Override
    public List<Message> findAllByChannelKey(UUID channelKey) {
        return data.values().stream()
                .filter(m -> m.getChannelKey().equals(channelKey))
                .toList();
    }

    @Override
    public UUID findKeyByMessageId(int messageId) {
        return data.values().stream()
                .filter(m -> m.getMessageId() == messageId)
                .map(Message::getUuid)
                .findFirst()
                .orElse(null);
    }
}
