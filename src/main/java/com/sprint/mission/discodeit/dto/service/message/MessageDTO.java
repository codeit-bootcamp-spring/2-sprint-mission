package com.sprint.mission.discodeit.dto.service.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record MessageDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        List<UUID> attachmentIds,
        String content,
        UUID channelId,
        UUID authorId
) {
}
