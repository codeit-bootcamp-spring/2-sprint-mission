package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final long createdAt;
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