package com.sprint.mission.discodeit.domain.user.dto;

import com.sprint.mission.discodeit.domain.user.entity.Role;
import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
