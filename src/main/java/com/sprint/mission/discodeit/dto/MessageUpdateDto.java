package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageUpdateDto {
    private UUID messageId;
    private String content;
}
