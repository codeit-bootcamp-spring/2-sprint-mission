package com.sprint.mission.discodeit.dto.controller.auth;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RoleUpdateRequest(
    @NotNull
    UUID userId,
    @NotNull
    Role newRole
) {

}
