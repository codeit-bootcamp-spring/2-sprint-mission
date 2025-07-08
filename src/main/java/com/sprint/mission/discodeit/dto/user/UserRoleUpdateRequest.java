package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
