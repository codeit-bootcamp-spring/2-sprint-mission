package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record MessageCreateRequestDTO(
        UUID userId,
        UUID channelId,
        String text
) {
}
