package com.sprint.mission.discodeit.service.dto.user.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusParam(
        UUID userId,
        Instant time
) {
}
