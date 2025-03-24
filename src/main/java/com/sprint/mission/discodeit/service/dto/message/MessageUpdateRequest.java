package com.sprint.mission.discodeit.service.dto.message;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID id,
        String newContent
) {
}
