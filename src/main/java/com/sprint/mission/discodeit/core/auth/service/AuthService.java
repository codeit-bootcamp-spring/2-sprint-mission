package com.sprint.mission.discodeit.core.auth.service;

import com.sprint.mission.discodeit.core.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;

public interface AuthService {

  UserDto initAdmin();

  UserDto updateRole(RoleUpdateRequest request);
}
