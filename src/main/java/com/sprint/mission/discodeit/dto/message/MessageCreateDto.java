package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateDto(
        UUID authorId,
        UUID channelId,
        String content
) {
}
