package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends Common{
    private String message;

    public Message() {
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
