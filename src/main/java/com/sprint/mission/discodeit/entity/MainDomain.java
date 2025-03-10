package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class MainDomain implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public MainDomain() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        update();
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getId() {
        return id;
    }

    protected void update() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
