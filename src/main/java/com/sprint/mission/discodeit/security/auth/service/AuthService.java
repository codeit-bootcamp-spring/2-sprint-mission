package com.sprint.mission.discodeit.security.auth.service;

import com.sprint.mission.discodeit.security.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;

public interface AuthService {

  UserResult initAdmin();

  UserResult updateRole(RoleUpdateRequest roleUpdateRequest);

}
