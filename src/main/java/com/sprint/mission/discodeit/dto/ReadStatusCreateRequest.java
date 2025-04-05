package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public ReadStatus convertCreateRequestToReadStatus() {
    return new ReadStatus(userId, channelId, lastReadAt);
  }

}
