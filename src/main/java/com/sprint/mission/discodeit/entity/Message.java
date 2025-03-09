package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends MainDomain {
    private String message;
    private final UUID userId;
    private final UUID channelId;



    public Message(String message,  UUID userId, UUID channelId) {
        super();
        this.message = message;
        this.userId = userId;
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }


    public void updateMessage(String newMessage) {
        boolean anyValueUpdated = false;
        if (newMessage != null && !newMessage.equals(message)) {
            message = newMessage;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            update();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "message=" + message + "\n" +
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}';

    }

}
