package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public BaseEntity() {
        this(UUID.randomUUID(), Instant.now());
    }

    public BaseEntity(UUID id) {
        this(id, Instant.now());
    }

    public BaseEntity(UUID id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void updateUpdatedAt(Instant updateTime) {
        this.updatedAt = updateTime;
    }
}
