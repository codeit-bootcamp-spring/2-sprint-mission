package com.sprint.mission.discodeit.core.status.usecase.read.dto;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusCommand(
    UUID readStatusId,
    Instant newLastReadAt
) {

}
