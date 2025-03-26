package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record CreateMessageRequestDTO(
        UUID userId,
        UUID channelId,
        String text
) {
}
