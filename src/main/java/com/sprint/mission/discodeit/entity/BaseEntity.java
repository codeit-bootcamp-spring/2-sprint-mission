package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private long createdAt;
    private long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    protected void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}