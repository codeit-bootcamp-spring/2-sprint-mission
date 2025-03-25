package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateReadStatusDto {
    private UUID id;
    private UUID channelId;
    private UUID userId;

    public UpdateReadStatusDto(UUID id, UUID channelId, UUID userId) {
        this.id = id;
        this.channelId = channelId;
        this.userId = userId;
    }

    public UpdateReadStatusDto(UUID channelId, UUID userId) {
        this.channelId = channelId;
        this.userId = userId;
    }
}
