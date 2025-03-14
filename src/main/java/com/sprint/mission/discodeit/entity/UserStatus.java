package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final long ONLINE_THRESHOLD_MINUTES = 5;

    private UUID id;
    private Instant createdAt;

    private Instant lastLoginAt;

    public UserStatus() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = Instant.now();
    }

    public boolean isOnline() {
        if (lastLoginAt == null) {
            return false;
        }
        long minutesAfterLogin = Duration.between(lastLoginAt, Instant.now()).toMinutes();
        return minutesAfterLogin < ONLINE_THRESHOLD_MINUTES;
    }
}
