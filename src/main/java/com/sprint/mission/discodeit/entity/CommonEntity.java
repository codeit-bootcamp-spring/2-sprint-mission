package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class CommonEntity {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public CommonEntity(Long currentTime) {
        this.id = UUID.randomUUID();
        this.createdAt = currentTime;
        this.updatedAt = createdAt;
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

    public void updateTime(Long currentTime) {
        this.updatedAt = currentTime;
    }
}