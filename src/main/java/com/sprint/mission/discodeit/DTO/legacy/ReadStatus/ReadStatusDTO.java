package com.sprint.mission.discodeit.DTO.legacy.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDTO(
        UUID readStatusId,
        UUID userId,
        UUID channelId,
        Instant createdAt,
        Instant updatedAt
) {
}
