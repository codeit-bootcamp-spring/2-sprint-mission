package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
  void initAdmin();
  UserDto login(LoginRequest loginRequest, HttpServletRequest request);
  void logout(HttpServletRequest request);
  UserDto updateRole(RoleUpdateRequest request);
}
