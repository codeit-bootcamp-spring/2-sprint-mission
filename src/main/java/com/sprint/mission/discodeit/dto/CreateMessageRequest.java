package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateMessageRequest {
    private UUID channelId;
    private String content;
    private List<String> filePath;
}
