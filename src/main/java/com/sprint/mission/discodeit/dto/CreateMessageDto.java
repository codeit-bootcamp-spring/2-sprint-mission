package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record CreateMessageDto(
        UUID channelKey,
        UUID userKey,
        String content,
        List<UUID> attachmentKeys
) {
}
