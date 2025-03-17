package com.sprint.mission.discodeit.dto.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        boolean isLogin
) {
}
