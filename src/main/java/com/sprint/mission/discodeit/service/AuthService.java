package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.controller.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import org.springframework.security.core.Authentication;

// 향후 OAuth 등의 인증이 추가될 수 있으므로 인터페이스 구현
public interface AuthService {

  UserDto initAdmin();

  UserDto updateRole(RoleUpdateRequest roleUpdateRequest);

}
