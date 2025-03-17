package com.sprint.discodeit.domain.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class UserStatus {

    private UUID id;
    private UUID userId;
    private Instant lastLoginTime;

    public UserStatus(UUID userId, Instant lastLoginTime) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastLoginTime = lastLoginTime;
    }
}
