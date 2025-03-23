package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.groups.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ReadStatusDto(
        @NotNull UUID id,
        @NotNull UUID userId,
        @NotNull UUID channelId,
        @NotNull Boolean readStatus
) {

    public static ReadStatusDto from(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getReadStatus()
        );
    }
}
