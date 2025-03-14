package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    private final UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public boolean isOnline() {
        Duration duration = Duration.between(Instant.now(), super.getUpdatedAt());
        return (duration.getSeconds() < 300);
    }
}
