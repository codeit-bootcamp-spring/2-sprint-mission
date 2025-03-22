package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record CreateMessageRequestDTO(
        UUID userId,
        UUID channelId,
        String text
) {
}
