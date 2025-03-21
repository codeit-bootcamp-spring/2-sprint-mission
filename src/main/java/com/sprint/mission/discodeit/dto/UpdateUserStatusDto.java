package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusDto(
        UUID userStatusKey,
        Instant lastActiveAt
) {
}
