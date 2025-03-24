package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private final UUID id;
    private UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public boolean isOnline(){
        return updatedAt.plus(Duration.ofMinutes(5)).isAfter(Instant.now());
    }

    public void updateStatus() {
        this.updatedAt = Instant.now();
    }
}
