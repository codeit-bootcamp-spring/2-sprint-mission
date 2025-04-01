package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record ServerCreateRequestDTO(
        UUID userId,
        String name
) {
}
