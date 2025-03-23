package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public ReadStatus convertCreateDtoToReadStatus(){
        return new ReadStatus(userId, channelId);
    }
}
