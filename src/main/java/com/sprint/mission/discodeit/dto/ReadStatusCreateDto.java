package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReadStatusCreateDto {
    private UUID channelId;
    private UUID userId;
}
