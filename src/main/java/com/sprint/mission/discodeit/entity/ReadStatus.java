package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus {
    protected UUID id;
    protected Instant createdAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = Instant.now();

    }
}