package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private UserStatusType status;

    private final UUID userId;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.status = UserStatusType.ONLINE;
    }

    public void update(UserStatusType status) {
        this.updatedAt = Instant.now();
        this.status = status;
    }

    public Boolean isOnline() {
        Instant now = Instant.now();
        if (status != UserStatusType.ONLINE && status != UserStatusType.OFFLINE) {
            return false;
        }
        if (Duration.between(updatedAt, now).toMinutes() <= 5) {
            status = UserStatusType.ONLINE;
            return true;
        } else {
            status = UserStatusType.OFFLINE;
            return false;
        }
    }
}