package com.sprint.mission.discodeit.service.messageDto;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest (
    UUID channelId,
    UUID userId,
    String content,
    List<String> attachments
){}