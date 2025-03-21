package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final UUID id;
    private UUID userId;
    private UUID channelId;
    private final Instant createdAt;
    private Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public boolean isRead(Instant latestMessageTime) {
        return !updatedAt.isBefore(latestMessageTime);
    }

    public void updateReadStatus() {
        this.updatedAt = Instant.now();
    }
}
