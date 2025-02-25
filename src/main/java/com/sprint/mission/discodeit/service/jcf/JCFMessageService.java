package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message createMessage(UUID userId, UUID channelId, String text) {
        Message message = new Message(userId, channelId, text);
        data.put(message.getId(), message);
        return message;
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
    public void updateMessage(UUID messageId, String newText) {
        Message message = data.get(messageId);
        if (message != null) {
            message.updateText(newText);
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }
}
