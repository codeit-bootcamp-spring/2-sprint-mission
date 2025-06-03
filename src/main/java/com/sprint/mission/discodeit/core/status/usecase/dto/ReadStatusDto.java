package com.sprint.mission.discodeit.core.status.usecase.dto;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusDto create(ReadStatus readStatus) {
    return ReadStatusDto.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
