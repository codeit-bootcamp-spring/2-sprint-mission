package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    private static final long ONLINE_THRESHOLD_MINUTES = 5;

    private UUID userId;

    private Instant lastLoginAt;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
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

    @Override
    public String toString() {
        return "UserStatus{" +
                "userId=" + userId +
                ", lastLoginAt=" + lastLoginAt +
                ", id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
