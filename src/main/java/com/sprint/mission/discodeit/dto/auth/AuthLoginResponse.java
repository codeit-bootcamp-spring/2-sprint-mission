package com.sprint.mission.discodeit.dto.auth;

import java.util.UUID;

public record AuthLoginResponse(
        UUID id,
        String email,
        String username
) {
}
