package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReadStatusCreateRequestDto {
    private UUID userId;
    private UUID channelId;

    public ReadStatusCreateRequestDto(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }
}
