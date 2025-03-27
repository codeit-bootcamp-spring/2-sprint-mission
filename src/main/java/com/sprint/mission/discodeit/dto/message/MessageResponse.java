package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID userId,
        UUID channelId,
        String text,
        List<UUID> attachmentIds,
        Instant createdAt,
        Instant updatedAt
) {}
