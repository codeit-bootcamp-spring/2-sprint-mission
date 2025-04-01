package com.sprint.mission.discodeit.service.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt
) {
}
