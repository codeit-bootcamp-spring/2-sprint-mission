package com.sprint.discodeit.domain.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;


@Getter
public class UserStatus {

    private UUID id;
    private UUID userId;
    private Instant lastLoginTime;
    private String statusType;

    public UserStatus(UUID userId, Instant lastLoginTime, String statusType) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastLoginTime = lastLoginTime;
        this.statusType = statusType;
    }
}
