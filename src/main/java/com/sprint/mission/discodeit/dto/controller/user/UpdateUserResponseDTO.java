package com.sprint.mission.discodeit.dto.controller.user;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserResponseDTO(
        UUID id,
        UUID profileId,
        Instant updatedAt,
        String username,
        String email
) {
}
