package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class CommonEntity {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public CommonEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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
    @Override
    public String toString() {
        return "ID: " + getId() + "\tCreated At: " + getCreatedAt() + "\tUpdated At: " + getUpdatedAt() + "]";
    }
}