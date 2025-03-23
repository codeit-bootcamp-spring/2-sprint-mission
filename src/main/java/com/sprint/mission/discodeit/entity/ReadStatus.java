package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadTime;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.lastReadTime = createdAt;
    }

    public void updateLastReadTime() {
        this.lastReadTime = ZonedDateTime.now().toInstant();
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
