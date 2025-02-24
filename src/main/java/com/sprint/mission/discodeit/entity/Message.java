package com.sprint.mission.discodeit.entity;

public class Message extends Common{
    private String message;

    public Message(String s) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void update(String message){
        this.message = message;
        updateUpdatedAt();
    }
}
