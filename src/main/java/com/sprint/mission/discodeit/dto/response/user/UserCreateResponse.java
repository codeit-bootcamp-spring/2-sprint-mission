package com.sprint.mission.discodeit.dto.response.user;

import java.time.Instant;
import java.util.UUID;

public record UserCreateResponse(
        String username,
        String email,
        UUID profileId,
        UUID id,
        Instant createdAt,
        Instant updatedAt
) {
}
