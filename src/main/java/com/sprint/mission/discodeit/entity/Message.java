package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends Common{
    private final UUID userId;
    private final UUID channelId;
    private String message;

    public Message(UUID userId, UUID channelId, String message) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getMessage() {
        return message;
    }

    public void update(String message){
        this.message = message;
        updateUpdatedAt();
    }
}
