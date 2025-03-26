package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    protected void updateUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
