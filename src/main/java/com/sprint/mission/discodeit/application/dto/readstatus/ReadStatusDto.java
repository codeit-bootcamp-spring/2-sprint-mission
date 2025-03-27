package com.sprint.mission.discodeit.application.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public record ReadStatusDto(UUID id, UUID userId, UUID channelId) {
    public static ReadStatusDto fromEntity(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId());
    }
}
