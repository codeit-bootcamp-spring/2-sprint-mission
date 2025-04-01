package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ReadStatusResponseDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public static ReadStatusResponseDto convertToResponseDto(ReadStatus readStatus) {
        return new ReadStatusResponseDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt());
    }
}
