package com.sprint.mission.discodeit.dto.display;

import java.time.Instant;
import java.util.UUID;

public record UserDisplayItem(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {
}
