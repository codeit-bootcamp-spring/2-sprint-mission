package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record FindUserDto(
        UUID userUUID,
        String nickname,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLoginTime,
        boolean online
) {
}
