package com.sprint.mission.discodeit.dto.service.message;

import java.util.UUID;

public record UpdateMessageParam(
        UUID id,
        String content
) {
}
