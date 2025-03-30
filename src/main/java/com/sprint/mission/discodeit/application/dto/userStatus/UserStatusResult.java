package com.sprint.mission.discodeit.application.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResult(UUID id, UUID userId, Instant lastLoginAt, boolean isLogin) {
    public static UserStatusResult fromEntity(UserStatus userStatus, boolean isLogin) {
        return new UserStatusResult(userStatus.getId(), userStatus.getUserId(), userStatus.getLastLoginAt(), isLogin);
    }
}

