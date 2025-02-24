package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String message;

    public Message(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Message updateMessage(String message) {
        this.message = message;

        return this;
    }
}
