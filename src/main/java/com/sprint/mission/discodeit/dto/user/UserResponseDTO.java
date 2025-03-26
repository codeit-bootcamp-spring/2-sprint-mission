package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID userId,
        Instant createdAt,
        Instant updatedAt,
        String userName,
        String email,
        UUID profiledId,
        Boolean isOnline
) {
}
