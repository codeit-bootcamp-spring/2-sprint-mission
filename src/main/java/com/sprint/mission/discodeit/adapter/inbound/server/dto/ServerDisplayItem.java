package com.sprint.mission.discodeit.adapter.inbound.server.dto;

import java.time.Instant;
import java.util.UUID;

public record ServerDisplayItem(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
