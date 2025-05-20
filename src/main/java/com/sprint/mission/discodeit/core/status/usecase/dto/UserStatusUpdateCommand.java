package com.sprint.mission.discodeit.core.status.usecase.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateCommand(
    UUID userId,
    UUID userStatusId,
    Instant newLastActiveAt
) {

}
