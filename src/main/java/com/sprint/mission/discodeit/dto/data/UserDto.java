package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID userKey,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileKey,
        Boolean online
) {
}
