package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private final UUID id;
    private UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID id, UUID userId, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public boolean isOnline() {
        return lastSeenAt.isAfter(Instant.now().minusSeconds(300));
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
    }
}
