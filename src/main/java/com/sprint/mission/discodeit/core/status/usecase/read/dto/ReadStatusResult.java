package com.sprint.mission.discodeit.core.status.usecase.read.dto;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
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

  public static ReadStatusResult create(ReadStatus readStatus) {
    return ReadStatusResult.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
