package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createAt;
    private final Instant updateAt;
    private final Instant lastActiveAt;

    public UserStatus(UUID id, Instant createAt, Instant updateAt, Instant lastActiveAt) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.lastActiveAt = lastActiveAt;
    }

    public UserStatus(Instant lastActiveAt) {
        this(UUID.randomUUID(), Instant.now(), Instant.now(), lastActiveAt);
    }

    private static final long ONLINE_THRESHOLD_SECONDS = 300;

    public boolean isOnline() {
        return lastActiveAt != null &&
                Duration.between(lastActiveAt, Instant.now()).toSeconds() < ONLINE_THRESHOLD_SECONDS;
    }
}
