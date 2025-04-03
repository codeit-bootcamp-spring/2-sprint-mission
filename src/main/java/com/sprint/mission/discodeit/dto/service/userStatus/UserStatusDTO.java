package com.sprint.mission.discodeit.dto.service.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDTO(
    UUID id,
    UUID userId,
    Instant cratedAt,
    Instant lastActiveAt
) {

}
