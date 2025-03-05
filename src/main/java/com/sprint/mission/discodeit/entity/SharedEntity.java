package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public abstract class SharedEntity {
    protected final UUID uuid;
    protected long createdAt;
    protected long updatedAt;

    public SharedEntity() {
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
