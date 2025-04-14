package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusDto from(com.sprint.mission.discodeit.entity.ReadStatus readStatus) {
    if (readStatus == null) {
      return null;
    }

    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }
}
