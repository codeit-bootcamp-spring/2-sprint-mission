package com.sprint.mission.discodeit.DTO.Request;

import java.util.UUID;

public record MessageWriteDTO(
        String creatorId,
        String channelId,
        String text
) {
}
