package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusDto(
     UUID id,
     UUID userId,
     UUID channelId,
     Instant lastReadAt
) {
}
