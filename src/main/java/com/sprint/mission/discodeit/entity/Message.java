package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private UUID sender;
    private String content;

    protected Message(UUID sender, String content) {
        super();
        setSender(sender);
        setContent(content);
    }

    private void setSender(UUID sender) {
        if (sender == null) {
            throw new IllegalArgumentException("유효하지 않은 송신자입니다");
        }
        this.sender = sender;
    }

    private void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 메세지입니다.");
        }
        this.content = content;
    }

    public UUID getSender() {
        return sender;
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
        return "Message{" +
                "sender=" + sender +
                ", content='" + content + '\'' +
                super.toString() +
                '}';
    }
}

