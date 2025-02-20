package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Common {
    protected UUID id;
    protected Long createdAt;
    protected Long updatedAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    protected void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

}
