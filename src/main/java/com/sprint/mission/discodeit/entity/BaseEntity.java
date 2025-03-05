package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
    private final UUID id = UUID.randomUUID();
    private final Long createdAt = System.currentTimeMillis();
    private Long updatedAt;

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateTime() {
        this.updatedAt = System.currentTimeMillis();
    }
}
