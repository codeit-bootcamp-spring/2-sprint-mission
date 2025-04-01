package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusDTO(
        UUID userId,
        UUID channelId,
        Instant readAt
) {
}
