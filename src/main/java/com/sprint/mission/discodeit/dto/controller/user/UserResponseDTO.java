package com.sprint.mission.discodeit.dto.controller.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        UUID profileId,
        String username,
        String email,
        Instant createdAt,
        boolean isLogin
) {
}
