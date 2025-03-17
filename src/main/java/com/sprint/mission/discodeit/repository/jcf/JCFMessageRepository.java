package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public Message save(Message message) {
        messageData.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId)).toList();
        if(messages.isEmpty() || messages == null){
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
        if(messages.isEmpty() || messages == null){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId) && m.getChannelId().equals(channelId)).toList();
        if (messages.isEmpty() || messages == null) {
            throw new NoSuchElementException("유저 " + userId + " 혹은 채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }

    @Override
    public Message update(UUID id, String newMessage) {
        Message messageNullable = messageData.get(id);
        Message message = Optional.ofNullable(messageNullable).orElseThrow(() -> new NoSuchElementException("메세지 " + id + "가 존재하지 않습니다."));
        message.updateMessage(newMessage);

        return message;
    }

    @Override
    public void delete(UUID id) {
        messageData.remove(id);
    }
}
