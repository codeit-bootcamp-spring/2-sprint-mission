package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private Long createdAt;
    private Long modifiedAt;

    public BaseEntity(UUID id, Long createdAt, Long modifiedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
