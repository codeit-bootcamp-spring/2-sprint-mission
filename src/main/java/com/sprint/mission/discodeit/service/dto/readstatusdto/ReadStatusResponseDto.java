package com.sprint.mission.discodeit.service.dto.readstatusdto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {

}
