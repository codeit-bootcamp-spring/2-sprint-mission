package com.sprint.mission.discodeit.domain.readstatus.dto;

import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadStatusResult(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt,
    Boolean notificationEnabled
) {

  public static ReadStatusResult from(ReadStatus readStatus) {
    return new ReadStatusResult(
        readStatus.getId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadTime(),
        readStatus.getNotificationEnabled()
    );
  }

  public static List<ReadStatusResult> from(List<ReadStatus> readStatuses) {
    return readStatuses.stream()
        .map(ReadStatusResult::from)
        .toList();
  }

}
