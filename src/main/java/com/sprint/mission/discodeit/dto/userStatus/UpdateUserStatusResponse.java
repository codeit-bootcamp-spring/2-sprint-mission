package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusResponse(
    UUID id,
    UUID userId,
    Instant lastActiveAt,
    boolean online,
    Instant createdAt,
    Instant updatedAt
) {

}
