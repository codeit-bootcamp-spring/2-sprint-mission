package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private static final JCFMessageRepository instance = new JCFMessageRepository();
    private final Map<UUID, Message> data = new HashMap<>();

    private JCFMessageRepository() {}

    public static JCFMessageRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> getAllMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }
}
