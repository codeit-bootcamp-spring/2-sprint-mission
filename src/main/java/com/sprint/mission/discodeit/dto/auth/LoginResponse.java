package com.sprint.mission.discodeit.dto.auth;

import java.util.UUID;

public record LoginResponse(
    UUID userId,
    String username,
    String email,
    UUID profile
) {

}
