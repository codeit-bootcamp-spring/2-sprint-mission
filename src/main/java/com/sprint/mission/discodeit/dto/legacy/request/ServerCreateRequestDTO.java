package com.sprint.mission.discodeit.dto.legacy.request;

import java.util.UUID;

public record ServerCreateRequestDTO(
        UUID ownerId,

        String name) {
}
