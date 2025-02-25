package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends CommonEntity {
    private UUID userId;
    private UUID channelId;

    public Message(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "[Message ID: " + getId() + "\tCreated At: " + getCreatedAt() + "\tUpdated At: " + getUpdatedAt() + "]";
    }
}
