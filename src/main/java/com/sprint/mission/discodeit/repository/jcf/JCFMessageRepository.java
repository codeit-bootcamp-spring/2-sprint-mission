package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return messages.values()
                .stream()
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messages.get(id)
                .updateContext(context);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> sameChannelMessageIds = messages.values()
                .stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();

        for (UUID messageId : sameChannelMessageIds) {
            messages.remove(messageId);
        }
    }
}
