package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record CreateServerRequestDTO(
        UUID userId,
        String name
) {
}
