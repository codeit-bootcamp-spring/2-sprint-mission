package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;

public interface AuthService {

  UserDto updateUserRole(RoleUpdateRequest request);

  UserDto initAdmin();
}
