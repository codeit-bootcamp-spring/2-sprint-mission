package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record FindUserDto(
        UUID userUUID,
        String nickname,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLoginTime,
        UserStatusType userStatus
) {
}
