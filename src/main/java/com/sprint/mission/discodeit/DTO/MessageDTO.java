package com.sprint.mission.discodeit.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDTO(
        UUID senderId,
        UUID channelId,
        String content) {
}
