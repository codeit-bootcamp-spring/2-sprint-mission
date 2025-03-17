package com.sprint.mission.discodeit.service.messageDto;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId,
        String content) {
}
