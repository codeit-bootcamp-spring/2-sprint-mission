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

    private final UUID userId;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
    }

    public void update() {
        this.updatedAt = Instant.now();
    }

    public UserStatusType isOnline() {
        Instant now = Instant.now();
        if (Duration.between(updatedAt, now).toMinutes() <= 5) {
            return UserStatusType.ONLINE;
        } else {
            return UserStatusType.OFFLINE;
        }
    }

}
