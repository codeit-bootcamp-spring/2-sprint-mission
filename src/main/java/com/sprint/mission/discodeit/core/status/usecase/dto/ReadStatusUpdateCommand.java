package com.sprint.mission.discodeit.core.status.usecase.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateCommand(
    UUID readStatusId,
    Instant newLastReadAt
) {

}
