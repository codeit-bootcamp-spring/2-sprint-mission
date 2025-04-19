package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseUpdatableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_DURATION = 5 * 60 * 1000;
    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public boolean isOnline(Instant now) {
        return Duration.between(this.lastActiveAt, now).toMillis() <= DEFAULT_DURATION;
    }
}
