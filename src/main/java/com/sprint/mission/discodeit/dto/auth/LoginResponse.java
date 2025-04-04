package com.sprint.mission.discodeit.dto.auth;

import java.time.Instant;
import java.util.UUID;

public record LoginResponse(
    UUID id,
    String username,
    String email,
    Instant createdAt,
    Instant updatedAt,
    UUID profileId
) {

}
