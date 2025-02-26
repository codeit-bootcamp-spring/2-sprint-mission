package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class MessageRepository {
    private final LinkedList<Message> messages = new LinkedList<>();

    private static class SingletonHolder {
        private static final MessageRepository INSTANCE = new MessageRepository();
    }

    private MessageRepository() {}

    public static MessageRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addMessage(Message newMessage) {
        if (newMessage == null) {
            throw new IllegalArgumentException("input newMessage is null!!!");
        }
        messages.addFirst(newMessage);
    }

    public Message findMessageByMessageId(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("null 값을 가진 messageId가 입력되었습니다!!!");
        }
        return messages.stream()
                .filter((m) -> Objects.equals(messageId, m.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 messageId 를 가진 message 를 찾을 수 없습니다!!!"));
    }

    public List<Message> findMessageListByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        return messages.stream()
                .filter((m) -> Objects.equals(channelId, m.getChannel().getId()))
                .collect(Collectors.toList());
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public void deleteMessage(UUID messageId) {
        boolean removed = messages.removeIf((m) -> Objects.equals(m.getId(), messageId));
        if (!removed) {
            throw new IllegalArgumentException("삭제할 messageId를 가진 message 가 존재하지 않습니다!!!");
        }
    }
}
