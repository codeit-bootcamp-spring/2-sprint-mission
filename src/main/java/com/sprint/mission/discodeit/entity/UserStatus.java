package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;

    private UserStatusType status;

    public UserStatus(UUID userId, UserStatusType status) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.status = status;
    }

    public void update(UserStatusType status) {
        this.updatedAt = Instant.now();
        this.status = status;
    }
}
