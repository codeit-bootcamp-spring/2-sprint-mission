package com.sprint.mission.discodeit.application.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record UserStatusResult(UUID id, UUID userId, Instant lastLoginAt, boolean isLogin) {
    public static UserStatusResult fromEntity(UserStatus userStatus, boolean isLogin) {
        return new UserStatusResult(userStatus.getId(), userStatus.getUserId(), userStatus.getLastLoginAt(), isLogin);
    }

    public static List<UserStatusResult> fromEntity(List<UserStatus> userStatuses) {
        return userStatuses.stream()
                .map(userStatus -> UserStatusResult.fromEntity(userStatus, userStatus.isLogin(ZonedDateTime.now().toInstant())))
                .toList(); // TODO: 3/30/25  수정 필요
    }
}

