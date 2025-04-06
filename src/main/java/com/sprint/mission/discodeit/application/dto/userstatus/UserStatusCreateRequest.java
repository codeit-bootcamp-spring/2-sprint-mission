package com.sprint.mission.discodeit.application.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant lastActiveAt
) {
}
