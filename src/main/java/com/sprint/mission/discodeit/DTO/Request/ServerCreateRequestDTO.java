package com.sprint.mission.discodeit.DTO.Request;

import java.util.UUID;

public record ServerCreateRequestDTO(
        UUID ownerId,

        String name) {
}
