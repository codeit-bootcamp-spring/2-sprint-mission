package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReadStatusUpdateDto {
    private UUID id;
    private UUID channelId;
    private UUID userId;

    public ReadStatusUpdateDto(UUID id, UUID channelId, UUID userId) {
        this.id = id;
        this.channelId = channelId;
        this.userId = userId;
    }

    public ReadStatusUpdateDto(UUID channelId, UUID userId) {
        this.channelId = channelId;
        this.userId = userId;
    }
}
