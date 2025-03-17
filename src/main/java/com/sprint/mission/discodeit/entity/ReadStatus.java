package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private UUID id;
    private UUID userId;
    private UUID channelId;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;

    @Builder
    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastReadAt = lastReadAt;
    }
}
