package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.UUID;

public record MessageDeleteResponse (
    UUID messageId,
    Instant deletedTime
) {
}
