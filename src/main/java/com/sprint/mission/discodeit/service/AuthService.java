package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  UserDto login(LoginRequest loginRequest, HttpServletRequest request);
  void logout(HttpServletRequest request);
}
