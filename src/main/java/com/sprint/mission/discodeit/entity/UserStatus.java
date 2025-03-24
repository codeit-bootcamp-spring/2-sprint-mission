package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private Instant lastActiveAt;
    private boolean isOnline;
    private static final long MAX_INACTIVITY_MINUTES = 5;


    public UserStatus(UUID userId, Instant lastActiveAt, boolean isOnline) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
        this.isOnline = isCurrentlyOnline();
    }

    public boolean isCurrentlyOnline() {
        Instant now = Instant.now();
        Duration duration = Duration.between(lastActiveAt, now);
        return duration.toMinutes() <= MAX_INACTIVITY_MINUTES;
    }

    public void update(Instant newLastActiveAt, boolean newIsOnline) {
        boolean anyValueUpdated = false;
        if (newLastActiveAt != null && !newLastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = newLastActiveAt;
            anyValueUpdated = true;
        }
        if (newIsOnline != this.isOnline) {
            this.isOnline = newIsOnline;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            update();
        }
    }
}
