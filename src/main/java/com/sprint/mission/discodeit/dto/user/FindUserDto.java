package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record FindUserDto(
    UUID id,
    String username,
    String email,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt,
    Instant lastLoginTime,
    boolean online
) {

}
