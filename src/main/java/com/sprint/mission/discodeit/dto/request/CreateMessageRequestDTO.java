package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record CreateMessageRequestDTO(
        UUID creatorId,
        UUID channelId,
        String text
) {
}
