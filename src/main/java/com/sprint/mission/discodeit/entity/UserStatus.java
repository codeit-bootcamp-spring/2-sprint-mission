package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.service.userDto.UserUpdateRequest;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID userId;
    private Instant lastActiveAt;
    protected UUID id;
    protected Instant createdAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
        this.id = UUID.randomUUID();
    }
    public boolean isOnline() {

        return lastActiveAt.isAfter(Instant.now().minusSeconds(300));
    }

    public void update(Instant newLastActiveAt) {
        lastActiveAt = newLastActiveAt;
    }
}