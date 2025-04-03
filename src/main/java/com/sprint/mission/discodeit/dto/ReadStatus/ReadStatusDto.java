package com.sprint.mission.discodeit.dto.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastReadAt
) {}

