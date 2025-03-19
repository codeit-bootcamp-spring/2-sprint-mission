package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID channelId;
    private final UUID userId;
    private String context;

    public Message(String context, UUID channelId, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.context = context;
    }

    public void updateContext(String context) {
        this.context = context;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
