package com.sprint.mission.discodeit.dto.MessageService;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId, String newMessage
) {
}
