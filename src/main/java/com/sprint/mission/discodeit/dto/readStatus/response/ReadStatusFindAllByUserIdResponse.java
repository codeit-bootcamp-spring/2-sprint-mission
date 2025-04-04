package com.sprint.mission.discodeit.dto.readStatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusFindAllByUserIdResponse(
        @NotNull UUID id,
        @NotNull UUID userId,
        @NotNull UUID channelId,
        @NotNull Boolean readStatus
) {

    public static ReadStatusFindAllByUserIdResponse from(ReadStatus readStatus) {
        return new ReadStatusFindAllByUserIdResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getReadStatus()
        );
    }
}
