package com.sprint.mission.discodeit.dto.update;

import java.time.Instant;

public record ReadStatusUpdateRequestDTO(
        Instant newLastReadAt
) {
}
