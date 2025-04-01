package com.sprint.mission.discodeit.dto.create;

import java.time.Instant;

public record ReadStatusCreateRequestDTO(
        Instant lastReadAt
) {
}
