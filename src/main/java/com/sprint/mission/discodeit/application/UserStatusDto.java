package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id, UUID userId, Instant lastLoginAt, boolean isLogin) {
    public static UserStatusDto fromEntity(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUserId(), userStatus.getLastLoginAt(), userStatus.isLogin());
    }
}

