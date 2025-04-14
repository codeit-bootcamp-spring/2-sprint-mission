package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusResponse(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusResponse of(ReadStatus readStatus) {
    return ReadStatusResponse.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUserId())
        .channelId(readStatus.getChannelId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
