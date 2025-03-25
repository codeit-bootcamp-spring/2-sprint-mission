package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusDTO(
        UUID readStatusId,
        Instant readAt
) {
}
