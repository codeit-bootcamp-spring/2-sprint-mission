package com.sprint.mission.discodeit.core.message.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class ReadStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID readStatusId;
    private final UUID userId;
    private final UUID channelId;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this(UUID.randomUUID(), userId, channelId, lastReadAt,Instant.now());
    }

    public ReadStatus(UUID readStatusId, UUID userId, UUID channelId, Instant lastReadAt, Instant createdAt) {
        this.readStatusId = readStatusId;
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        this.updatedAt = Instant.now();
        this.lastReadAt = lastReadAt;
    }
}
