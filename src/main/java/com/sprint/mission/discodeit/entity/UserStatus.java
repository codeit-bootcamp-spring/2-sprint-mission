package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID userId;
    private Instant lastActiveAt;
    protected UUID id;
    protected Instant createdAt;

    public UserStatus(User user, Instant lastActiveAt) {
        this.userId = user.getId();
        this.lastActiveAt = lastActiveAt;
    }
    public boolean isOnline() {
        return lastActiveAt.isAfter(Instant.now().minusSeconds(300));
    }
}