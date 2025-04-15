package com.sprint.mission.discodeit.core.status.usecase.read.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusResult(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusResult create(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {
    return ReadStatusResult.builder()
        .id(id)
        .userId(userId)
        .channelId(channelId)
        .lastReadAt(lastReadAt)
        .build();
  }
}
