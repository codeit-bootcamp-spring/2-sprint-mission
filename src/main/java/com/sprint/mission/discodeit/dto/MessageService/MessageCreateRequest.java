package com.sprint.mission.discodeit.dto.MessageService;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageCreateRequest(
        String message,
        UUID userId,
        UUID channelId
) {
}
