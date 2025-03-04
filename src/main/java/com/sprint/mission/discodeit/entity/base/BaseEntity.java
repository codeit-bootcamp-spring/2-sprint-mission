package com.sprint.mission.discodeit.entity.base;

import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    private final Long createdAt;
    private Long modifiedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void updateModifiedAt() {
        this.modifiedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return ", UUID=" + id +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt
                ;
    }
}
