package com.sprint.mission.discodeit.dto.service.readStatus;

import java.util.UUID;

public record ReadStatusDTO(
        UUID id,
        UUID userId,
        UUID channelId
) {
}
