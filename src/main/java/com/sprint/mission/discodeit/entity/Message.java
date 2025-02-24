package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends Common {
    private String sender; // 메시지를 받는 사람
    private String message; // 메시지 내용

    public Message(String sender) {
        this.sender = sender;
    }

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;

    }

    public String getSender() {
        return sender;
    }
    public String getMessage() {
        return message;
    }


    public void updateMessage(String message) {
        this.message = message;
        super.update();
    }
}
