package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends SharedEntity{
    private final UUID userKey;
    private Instant lastActiveAt;

    public UserStatus(UUID userKey, Instant lastActiveAt) {
        super();
        this.userKey = userKey;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        setUpdatedAt(Instant.now());
    }

    public boolean isOnline() {
        return lastActiveAt != null && Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
    }
}
