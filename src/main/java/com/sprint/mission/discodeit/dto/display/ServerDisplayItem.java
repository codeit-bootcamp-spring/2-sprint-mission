package com.sprint.mission.discodeit.dto.display;

import java.time.Instant;
import java.util.UUID;

public record ServerDisplayItem(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
