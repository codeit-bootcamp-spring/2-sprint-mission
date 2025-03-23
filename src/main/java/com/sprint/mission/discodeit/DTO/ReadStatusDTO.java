package com.sprint.mission.discodeit.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadStatusDTO(
        UUID userId,
        UUID channelId,
        LocalDateTime lastReadTime) {
}
