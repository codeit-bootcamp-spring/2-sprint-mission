package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.user.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;

public interface AuthService {

  UserDto initAdmin();

  UserDto testUser();

  UserDto updateRole(RoleUpdateRequest request);
}
