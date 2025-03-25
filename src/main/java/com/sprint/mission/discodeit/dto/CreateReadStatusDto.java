package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReadStatusDto {
    private UUID channelId;
    private UUID userId;
}
