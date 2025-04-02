package com.sprint.mission.discodeit.core.status.usecase.user.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateUserStatusCommand(
    UUID userId,
    Instant lastActiveAt
) {

}
