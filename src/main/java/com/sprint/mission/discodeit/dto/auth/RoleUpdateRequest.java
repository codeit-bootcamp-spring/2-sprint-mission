package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.constant.Role;
import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
