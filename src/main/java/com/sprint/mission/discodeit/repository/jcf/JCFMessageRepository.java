package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
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
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Message> findAllByChannelKey(UUID channelKey) {
        return data.values().stream().filter(m -> m.getChannelKey().equals(channelKey)).toList();
    }

    @Override
    public boolean existsByKey(UUID messageKey) {
        return data.containsKey(messageKey);
    }
}
