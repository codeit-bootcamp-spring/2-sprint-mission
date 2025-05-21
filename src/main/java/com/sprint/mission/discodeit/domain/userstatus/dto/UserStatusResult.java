package com.sprint.mission.discodeit.domain.userstatus.dto;

import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResult(UUID id, Instant createdAt, Instant updatedAt, UUID userId, Instant lastActiveAt,
                               boolean online) {
    public static UserStatusResult fromEntity(UserStatus userStatus, boolean online) {
        return new UserStatusResult(userStatus.getId(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt(),
                online);
    }
}

