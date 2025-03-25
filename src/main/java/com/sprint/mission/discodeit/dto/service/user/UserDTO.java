package com.sprint.mission.discodeit.dto.service.user;

import java.time.Instant;
import java.util.UUID;


public record UserDTO(
        UUID id,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        Boolean isLogin
) {
}
