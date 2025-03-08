package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void createMessage(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> selectMessageById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> selectMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId) {
        Message message = data.get(id);
        message.updateContent(content);
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        data.remove(id);
    }
}
