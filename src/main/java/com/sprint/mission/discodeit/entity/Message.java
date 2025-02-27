package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private final UUID channelId;
    private final UUID userId;
    private String context;

    public Message(String context, UUID channelId, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.context = context;
    }

    public void updateContext(String context) {
        this.context = context;
        updatedAt();
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public String getContext() {
        return context;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getUserId() {
        return userId;
    }
}
