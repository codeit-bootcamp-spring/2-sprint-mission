package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class ReadStatus {
    private final UUID readStatusId;
    private final UUID userId;
    private final UUID channelId;
    public final Instant createdAt;
    public Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this(UUID.randomUUID(), userId, channelId, Instant.now());
    }

    public ReadStatus(UUID readStatusId, UUID userId, UUID channelId, Instant createdAt) {
        this.readStatusId = readStatusId;
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }
}
