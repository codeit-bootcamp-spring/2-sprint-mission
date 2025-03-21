package com.sprint.mission.discodeit.dto.legacy.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDTO(
        UUID serverId,
        UUID channelId,
        UUID messageId,
        UUID creatorId,
        String creatorName,
        String text,
        Instant createdAt,
        Instant updatedAt,
        List<UUID> attachmentIds,
        List<BinaryContent> binaryContent
) {
}
