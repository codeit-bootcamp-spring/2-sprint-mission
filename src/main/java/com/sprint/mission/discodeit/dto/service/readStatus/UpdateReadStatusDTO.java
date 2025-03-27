package com.sprint.mission.discodeit.dto.service.readStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusDTO(
        UUID id,
        Instant updatedAt
) {
}
