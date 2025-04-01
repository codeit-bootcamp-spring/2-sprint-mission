package com.sprint.mission.discodeit.adapter.inbound.server.dto;

import java.util.UUID;

public record ServerCreateRequestDTO(
        UUID userId,
        String name
) {
}
