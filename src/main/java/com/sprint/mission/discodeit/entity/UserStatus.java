package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    private final UUID userId;
    private Instant lastAccessAt;

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.lastAccessAt = Instant.now();
    }

    public void updateLastAccessAt(Instant lastAccessAt) {
        this.lastAccessAt = lastAccessAt;
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return lastAccessAt.isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
    }
}
