package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class UserStatus {
    private UUID userStatusId;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private Instant lastActiveAt;


    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userStatusId = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;

        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public boolean isOnline() {
        return Instant.now().minusSeconds(300).isBefore(lastActiveAt);
    }
    public void updateLastActiveAt(Instant lastActiveAt){
        this.lastActiveAt = lastActiveAt;
        this.updatedAt = Instant.now();
    }
}
