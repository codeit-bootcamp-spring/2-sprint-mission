package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_DURATION = 5 * 60 * 1000;
    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        updateLastModified();
    }

    public boolean isOnline(Instant now) {
        return Duration.between(this.lastActiveAt, now).toMillis() <= DEFAULT_DURATION;
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
