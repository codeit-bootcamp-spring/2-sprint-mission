package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class UserStatus {
    private final UUID userStatusId;
    private final UUID userId;
    public final Instant createdAt;
    public Instant updatedAt;
    public UserStatusType status;

    public UserStatus(UUID userId) {
        this(UUID.randomUUID(), userId, Instant.now());
    }

    public UserStatus(UUID userStatusId, UUID userId, Instant createdAt) {
        this.userStatusId = userStatusId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public boolean isONLine() {
        Instant now = Instant.now();
        Duration duration = Duration.between(updatedAt, now);
        long minutes = duration.toMinutes();
        if (minutes <= 5) {
            this.status = UserStatusType.ONLINE;
            return true;
        } else {
            this.status = UserStatusType.OFFLINE;
            return false;
        }
    }
}
