package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageKey,
        String newContent
) {
}
