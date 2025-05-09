package com.sprint.mission.discodeit.readstatus.dto;

import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadStatusResult(UUID id,
                               Instant createdAt,
                               Instant updatedAt,
                               UUID userId,
                               UUID channelId,
                               Instant lastReadAt) {
    public static ReadStatusResult fromEntity(ReadStatus readStatus) {
        return new ReadStatusResult(
                readStatus.getId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadTime());
    }


    public static List<ReadStatusResult> fromEntity(List<ReadStatus> readStatuses) {
        return readStatuses.stream()
                .map(ReadStatusResult::fromEntity)
                .toList();
    }
}
