package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.security.Role;

public record UserRoleUpdateRequest(
    String username,
    Role role
) {

}
