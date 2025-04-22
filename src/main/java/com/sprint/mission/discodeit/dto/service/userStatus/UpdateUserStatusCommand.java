package com.sprint.mission.discodeit.dto.service.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusCommand(
    UUID userId,
    Instant newLastActiveAt
) {

}
