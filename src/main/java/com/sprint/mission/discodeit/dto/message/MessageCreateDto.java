package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        UUID authorId,
        UUID channelId,
        String content,
        List<UUID> attachmentIds
) {
}
