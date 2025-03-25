package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateMessageDto {
    private UUID messageId;
    private String content;
}
