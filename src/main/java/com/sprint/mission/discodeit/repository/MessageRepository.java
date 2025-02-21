package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class MessageRepository {
    private LinkedList<Message> messages;

    public MessageRepository() {
        messages = new LinkedList<>();
    }

    public void addMessage(Message newMessage) {
        if (newMessage == null) {}
        messages.addFirst(newMessage);
    }

    public Message findMessageById(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("null 값을 가진 messageId가 입력되었습니다!!!");
        }
        return messages.stream()
                .filter((m) -> Objects.equals(messageId, m.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 messageId 를 가진 message 를 찾을 수 없습니다!!!"));
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
