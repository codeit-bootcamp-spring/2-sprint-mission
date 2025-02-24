package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class MainDomain {
    private UUID id;
    private Long createdAt, updatedAt;

    public MainDomain() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
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
