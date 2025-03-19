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
        id = UUID.randomUUID();
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void updateUpdatedAt(Instant updateTime) {
        this.updatedAt = updateTime;
    }
}
