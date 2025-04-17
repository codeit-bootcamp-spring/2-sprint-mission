package com.sprint.mission.discodeit.dto.service.userStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateUserStatusCommand(
    UUID userId,
    Instant lastActiveAt
) {

}
