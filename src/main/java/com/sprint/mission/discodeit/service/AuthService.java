package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserDto getCurrentUser(Authentication authentication);

    UserDto updateUserRole(RoleUpdateRequest request);
}
