package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private final UUID senderId;
    private String content;

    protected Message(UUID senderId, String content) {
        super();
        validateSenderId(senderId);
        this.senderId = senderId;
        setContent(content);
    }

    private void validateSenderId(UUID senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("유효하지 않은 송신자입니다");
        }
    }

    private void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 메세지입니다.");
        }
        this.content = content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public void update(String content) {
        if (content != null) setContent(content);
        updateModifiedAt();
    }

    @Override
    public String toString() {
        return "sender=" + senderId +
                ", content='" + content + '\'' +
                super.toString();
    }
}

