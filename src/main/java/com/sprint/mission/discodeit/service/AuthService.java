package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {
    UserDto initAdmin();

    UserDto updateRole(RoleUpdateRequest roleUpdateRequest);
}