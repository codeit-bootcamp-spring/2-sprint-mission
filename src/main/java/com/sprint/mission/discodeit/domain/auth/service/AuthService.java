package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;

public interface AuthService {

  UserResult initAdmin();

  UserResult updateRole(RoleUpdateRequest roleUpdateRequest);

}
