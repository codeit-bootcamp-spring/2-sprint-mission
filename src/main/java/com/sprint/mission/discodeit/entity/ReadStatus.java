package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID readStatusId;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant readAt;

    public ReadStatus(UUID userId, UUID channelId, Instant readAt) {
        this.readStatusId = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.userId = userId;
        this.channelId = channelId;
        this.readAt = readAt;
    }

    public void updateReadAt(Instant newReadAt) {
        boolean anyValueUpdated = false;
        if (newReadAt != null && !newReadAt.equals(this.readAt)) {
            this.readAt = newReadAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
