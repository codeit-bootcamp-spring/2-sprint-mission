package com.sprint.mission.discodeit.service.dto.user.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant lastActiveAt
) {
}
