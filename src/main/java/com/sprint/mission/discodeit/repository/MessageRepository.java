package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.*;
import java.util.stream.Collectors;

public class MessageRepository implements Repository<Message> {
    private final HashMap<UUID, Message> messages = new LinkedHashMap<>();

    private static class SingletonHolder {
        private static final MessageRepository INSTANCE = new MessageRepository();
    }

    private MessageRepository() {}

    public static MessageRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void add(Message newMessage) {
        if (newMessage == null) {
            throw new IllegalArgumentException("input newMessage is null!!!");
        }
        messages.put(newMessage.getId(), newMessage);
    }

    @Override
    public boolean existsById(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("null값을 가지는 messageId가 들어왔습니다!!!");
        }
        return messages.containsKey(messageId);
    }

    @Override
    public Message findById(UUID messageId) {
        if (!existsById(messageId)) {
            throw new NoSuchElementException("해당 messageId를 가진 메세지를 찾을 수 없습니다 : " + messageId);
        }
        return messages.get(messageId);
    }

    @Override
    public HashMap<UUID, Message> getAll() {
        return messages;
    }

    @Override
    public void deleteById(UUID messageId) {
        if (!existsById(messageId)) {
            throw new NoSuchElementException("해당 channelId를 가진 메세지를 찾을 수 없습니다 : " + messageId);
        }
        messages.remove(messageId);
    }

    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        return messages.values().stream()
                .filter((m) -> Objects.equals(channelId, m.getChannel().getId()))
                .collect(Collectors.toList());
    }
}
