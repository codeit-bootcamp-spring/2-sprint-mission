package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageReadResponse (
        UUID messageId,
        String content,
        List<UUID> attachmentIds
) {
}
