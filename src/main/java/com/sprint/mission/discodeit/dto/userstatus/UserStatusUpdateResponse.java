package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateResponse(
        UUID userId,
        String username,
        boolean isOnline,
        Instant lastLoginAt
) {
}
