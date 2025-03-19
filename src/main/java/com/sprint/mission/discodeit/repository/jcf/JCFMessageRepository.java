package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    private JCFMessageRepository() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId) {
        Optional<Instant> latestMessageTime = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getUpdatedAt))
                .map(Message::getUpdatedAt);
        return latestMessageTime;
    }

    @Override
    public void deleteById(UUID messageId) {
        data.remove(messageId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<Message> messages = findAllByChannelId(channelId);
        messages.forEach(message -> data.remove(message.getId()));
    }
}
