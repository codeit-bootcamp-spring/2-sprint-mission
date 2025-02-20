package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public BaseEntity(UUID id, long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
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

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
