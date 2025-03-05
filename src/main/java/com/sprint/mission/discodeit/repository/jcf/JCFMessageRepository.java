package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();

    private void checkById(UUID messageId) {
        if (!(messageData.containsKey(messageId))) {
            throw new IllegalArgumentException("존재하지 않는 메시지ID입니다.");
        }
    }

    @Override
    public Message findById(UUID messageId) {
        checkById(messageId);
        return messageData.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        if (messageData.isEmpty()) {
            throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
        }
        return new ArrayList<>(messageData.values());
    }

    @Override
    public List<Message> findUpdatedMessages() {
        return messageData.values().stream()
                .filter(entry -> entry.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createMessage(Channel channel, User user, String messageContent) {
        Message message = new Message(channel, user, messageContent);
        this.messageData.put(message.getId(), message);
    }

    @Override
    public void updateMessage(UUID messageId, String messageContent) {
        checkById(messageId);
        Message message = this.messageData.get(messageId);
        message.messageUpdate(messageContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        checkById(messageId);
        this.messageData.remove(messageId);
    }

    @Override
    public void deleteMessagesByChannelId(UUID channelId) {
        List<UUID> removeId = messageData.values().stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .map(Message::getId)
                .toList();
        removeId.forEach(messageData::remove);
    }
}
