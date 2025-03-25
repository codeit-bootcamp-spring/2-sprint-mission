package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusCreateRequest {
    private UUID userId;
    private Instant lastActiveAt;

    @Builder
    public UserStatusCreateRequest(UUID userId, Instant lastActiveAt) {
        this.userId = UUID.randomUUID();
        this.lastActiveAt = Instant.now();
    }
}
