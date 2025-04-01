package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadTime;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadTime) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = lastReadTime;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateLastReadTime(Instant lastReadTime) {
        this.lastReadTime = lastReadTime;
        this.updatedAt = Instant.now();
    }


}
