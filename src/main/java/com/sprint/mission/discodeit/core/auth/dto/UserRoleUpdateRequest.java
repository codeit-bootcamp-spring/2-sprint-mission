package com.sprint.mission.discodeit.core.auth.dto;

import com.sprint.mission.discodeit.core.user.entity.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
