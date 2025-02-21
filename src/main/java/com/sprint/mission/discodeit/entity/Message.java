package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String context;

    public Message(String context) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = createdAt;
        this.context = context;
    }

    public void updateContext(String context) {
        this.context = context;
        updatedAt();
    }

    private void updatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public String getContext() {
        return context;
    }
}
