package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record CreateMessageDTO(
        String text,
        UUID userId,
        UUID channelId,
        List<UUID> attachmentIds
) {
}
