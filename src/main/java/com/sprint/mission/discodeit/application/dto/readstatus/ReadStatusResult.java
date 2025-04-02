package com.sprint.mission.discodeit.application.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadStatusResult(UUID readStatusId, UUID userId, UUID channelId, Instant lastReadTime) {
    public static ReadStatusResult fromEntity(ReadStatus readStatus) {
        return new ReadStatusResult(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }


    public static List<ReadStatusResult> fromEntity(List<ReadStatus> readStatuses) {
        return readStatuses.stream()
                .map(ReadStatusResult::fromEntity)
                .toList();
    }
}
