package com.sprint.mission.discodeit.service.dto.userstatusdto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt
) {
}
