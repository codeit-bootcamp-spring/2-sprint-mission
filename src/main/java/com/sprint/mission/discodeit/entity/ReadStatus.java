package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;
    private boolean isRead;

    public ReadStatus(UUID userId, UUID channelId, boolean isRead) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
        this.isRead = isRead;
    }

    public void updateLastReadAt() {
        this.lastReadAt = Instant.now();
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
