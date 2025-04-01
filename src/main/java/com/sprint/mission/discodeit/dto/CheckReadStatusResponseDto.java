package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record CheckReadStatusResponseDto (
        UUID channelUUID,
        String channelName,
        Instant lastMessageAt,
        Boolean isRead
) {
}
