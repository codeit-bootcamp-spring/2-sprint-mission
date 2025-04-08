package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Duration MAX_ACTIVE_MINUTES = Duration.ofMinutes(5);
    private final UUID userId;

    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updateUpdatedAt();
        }
    }

    public boolean isOnline() {
        Instant now = Instant.now();
        Duration duration = Duration.between(this.lastActiveAt, now);

        return duration.compareTo(MAX_ACTIVE_MINUTES) < 0; // 5분 이내면 true;
    }
}
