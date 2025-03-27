package com.sprint.mission.discodeit.dto.controller.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateMessageResponseDTO(
        UUID id,
        Instant updatedAt,
        List<UUID> attachmentIds,
        String content,
        UUID channelId,
        UUID authorId
) {
}
