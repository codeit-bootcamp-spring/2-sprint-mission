package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserFindDTO(
        UUID id,
        UUID profileId,
        String name,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
}
