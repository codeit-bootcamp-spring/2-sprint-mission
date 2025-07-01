package com.sprint.mission.discodeit.core.user.dto.request;

import com.sprint.mission.discodeit.core.user.entity.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
