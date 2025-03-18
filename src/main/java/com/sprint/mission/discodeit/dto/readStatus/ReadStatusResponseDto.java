package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponseDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
