package com.sprint.mission.discodeit.dto.service.user;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserDTO(
        UUID id,
        UUID profileId,
        Instant updatedAt,
        String username,
        String email
) {
}
