package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {

    private UUID readStatusId;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant readAt;

    public ReadStatus(UUID userId, UUID channelId,Instant readAt) {
        this.readStatusId = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.userId = userId;
        this.channelId = channelId;
        this.readAt = readAt;
    }

    public void updateReadAt(Instant readAt) {
        this.readAt = readAt;
        this.updatedAt = Instant.now();
    }
}
