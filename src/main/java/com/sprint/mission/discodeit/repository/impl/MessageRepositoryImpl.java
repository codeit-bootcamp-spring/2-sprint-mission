package com.sprint.mission.discodeit.repository.impl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class MessageRepositoryImpl implements MessageRepository {
    private final Map<UUID, Message> messageStore = new HashMap<>();

    @Override
    public Message save(Message message) {
        messageStore.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageStore.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageStore.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageStore.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return messageStore.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        messageStore.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messageStore.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }
}