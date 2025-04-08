package com.sprint.mission.discodeit.dto.response.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID userId,
    boolean online,
    Instant updatedAt
) {

}
