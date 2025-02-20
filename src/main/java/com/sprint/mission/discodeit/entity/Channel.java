package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Channel {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String name;

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt();
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public String getName() {
        return name;
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
}
