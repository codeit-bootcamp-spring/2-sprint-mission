package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;
    private Instant lastActivatedAt;
    private Status status;

    public UserStatus(UUID userId, Status online) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastActivatedAt = null;
    }

    public void updateLastActivatedAt(Instant newLastActivatedAt) {
        if (newLastActivatedAt != null && (this.lastActivatedAt == null || newLastActivatedAt.isAfter(this.lastActivatedAt))) {
            this.lastActivatedAt = newLastActivatedAt;
            this.updatedAt = Instant.now();
        }
    }

    public boolean isCurrentlyOnline() {
        if (this.lastActivatedAt == null) {
            System.out.println("User is offline");
            return false;
        }
        Instant now = Instant.now();
        Duration durationSinceLastActive = Duration.between(this.lastActivatedAt, now);
        if (durationSinceLastActive.toMinutes() <= 5) {
            System.out.println("User is online");
            return true;
        } else {
            System.out.println("User is offline");
            return false;
        }
    }

    public Status getStatus() {
        return isCurrentlyOnline() ? Status.ONLINE : Status.OFFLINE;
    }
}
