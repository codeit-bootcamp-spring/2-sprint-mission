package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class SharedEntity implements Serializable {
    protected final UUID uuid;
    protected Instant createdAt;
    protected Instant updatedAt;

    public SharedEntity() {
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = Instant.now();
    }
}
