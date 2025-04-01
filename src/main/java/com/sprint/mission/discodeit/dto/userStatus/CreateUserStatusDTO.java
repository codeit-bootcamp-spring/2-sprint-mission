package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateUserStatusDTO(
        UUID userId,
        Instant lastActiveAt
) {
}
