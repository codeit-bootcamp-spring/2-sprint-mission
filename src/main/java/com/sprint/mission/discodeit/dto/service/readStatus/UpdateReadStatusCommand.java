package com.sprint.mission.discodeit.dto.service.readStatus;

import java.time.Instant;

public record UpdateReadStatusCommand(
    Instant newLastReadAt
) {

}
