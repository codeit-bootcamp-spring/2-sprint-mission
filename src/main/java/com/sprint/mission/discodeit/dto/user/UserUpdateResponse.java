package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserUpdateResponse(
    UUID id,
    String username,
    String email,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt
) {

}
