package com.sprint.mission.discodeit.dto.service.auth;

import java.util.UUID;

public record LoginDTO(
        UUID id,
        String username
) {
}
