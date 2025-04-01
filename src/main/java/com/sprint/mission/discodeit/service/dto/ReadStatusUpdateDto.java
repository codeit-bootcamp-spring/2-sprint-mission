package com.sprint.mission.discodeit.service.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
