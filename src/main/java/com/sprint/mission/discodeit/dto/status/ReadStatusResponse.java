package com.sprint.mission.discodeit.dto.status;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt,
        Instant createdAt,
        Instant updatedAt
) {}
