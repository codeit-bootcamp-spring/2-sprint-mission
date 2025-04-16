package com.sprint.mission.discodeit.service.dto.readstatusdto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {

}
