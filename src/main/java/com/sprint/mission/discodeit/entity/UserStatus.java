package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity {
    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public boolean isActive() {
        Instant now = Instant.now();
        Instant lastActive = this.updatedAt;
        Duration duration = Duration.between(now, lastActive);

        return duration.getSeconds() < 300; // 5분 이내면 true;
    }
}
