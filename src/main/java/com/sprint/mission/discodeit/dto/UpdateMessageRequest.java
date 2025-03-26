package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateMessageRequest {
    private UUID messageId;
    private String content;
}
