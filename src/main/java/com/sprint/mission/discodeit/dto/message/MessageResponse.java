package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID authorId,
    UUID channelId,
    String content,
    List<UUID> attachmentIds,
    Instant createdAt,
    Instant updatedAt
) {

}
