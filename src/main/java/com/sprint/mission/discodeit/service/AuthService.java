package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;

public interface AuthService {

  void login(LoginRequest loginRequest);
}
