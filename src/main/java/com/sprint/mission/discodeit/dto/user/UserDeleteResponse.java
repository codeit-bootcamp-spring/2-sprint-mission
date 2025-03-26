package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDeleteResponse(
        UUID userId,
        Instant deletedTime
) {
}
