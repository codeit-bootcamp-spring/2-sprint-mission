package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record CreateServerRequestDTO(
        UUID userId,
        String name
) {
}
