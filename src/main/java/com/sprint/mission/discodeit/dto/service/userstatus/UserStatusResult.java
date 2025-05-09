package com.sprint.mission.discodeit.dto.service.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResult(UUID id, Instant createdAt, Instant updatedAt, UUID userId, Instant lastLoginAt,
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

