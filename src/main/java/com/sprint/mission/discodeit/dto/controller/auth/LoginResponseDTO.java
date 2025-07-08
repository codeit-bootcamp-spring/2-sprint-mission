package com.sprint.mission.discodeit.dto.controller.auth;


import com.sprint.mission.discodeit.entity.Role;
import java.time.Instant;
import java.util.UUID;

public record LoginResponseDTO(
    UUID id,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt,
    String username,
    Role role,
    String email,
    Boolean online
) {

}
