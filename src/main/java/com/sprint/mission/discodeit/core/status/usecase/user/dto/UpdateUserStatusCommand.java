package com.sprint.mission.discodeit.core.status.usecase.user.dto;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusCommand(
    UUID userId,
    UUID userStatusId,
    Instant newLastActiveAt
) {

}
