package com.sprint.mission.discodeit.entity;

public class Message extends Common{
    private String message;


    public Message(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void updateMessage(String message) {
        this.message = message;
        setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message [message=" + message + "]";
    }
}
