package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record MessageCreateRequestDTO(
        UUID channelId,
        String text
) {
}
