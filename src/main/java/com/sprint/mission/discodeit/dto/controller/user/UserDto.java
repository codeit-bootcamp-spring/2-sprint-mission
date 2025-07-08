package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.entity.Role;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt,
    String username,
    Role role,
    String email
) implements Serializable {

}
