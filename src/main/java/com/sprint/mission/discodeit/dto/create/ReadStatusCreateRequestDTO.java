package com.sprint.mission.discodeit.dto.create;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDTO(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
