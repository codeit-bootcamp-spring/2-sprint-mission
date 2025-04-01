package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;

public record UpdateUserStatusDTO(
        Instant lastActiveAt
) {
}
