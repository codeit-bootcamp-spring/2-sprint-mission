package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.Duration;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAtSeconds;
    private Instant updatedAtSeconds;
    private final UUID userId;
    private Instant lastActiveAt;
    private boolean isOnline;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.updatedAtSeconds = createdAtSeconds;
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }


    public void updatedLastActiveAt(Instant newLastActiveAt) {
        if (newLastActiveAt != null && newLastActiveAt.isAfter(this.lastActiveAt)){
            this.lastActiveAt = newLastActiveAt;
            this.updatedAtSeconds = Instant.now();
        }
    }

    public boolean isUserOnline() {
        Instant now = Instant.now();
        return Duration.between(lastActiveAt, now).getSeconds() <= 300;
    }
}