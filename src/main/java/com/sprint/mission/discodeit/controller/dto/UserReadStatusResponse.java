package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record UserReadStatusResponse(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public static UserReadStatusResponse of(ReadStatus readStatus) {
        return new UserReadStatusResponse(readStatus.getId(), readStatus.getUserId(),
                readStatus.getChannelId(), readStatus.getLastReadAt());
    }
}
