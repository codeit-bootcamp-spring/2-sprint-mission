package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private Long createdAt;
    private Long modifiedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void updateModifiedAt() {
        this.modifiedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
