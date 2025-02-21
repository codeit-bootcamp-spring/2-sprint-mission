package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends Common {
    private User sender; // 메시지를 받는 사람
    private String message; // 메시지 내용
    private Long sentAt; // 메시지 보낸시간


    public Message(User sender, String message) {
        this.sender = sender;
        this.message = message;
        this.sentAt = System.currentTimeMillis();
    }

    public User getSender() {
        return sender;
    }
    public String getMessage() {
        return message;
    }

    public Long getSentAt() {
        return sentAt;
    }

    public void updateMessage(String message) {
        this.message = message;
        super.update();
    }
}
