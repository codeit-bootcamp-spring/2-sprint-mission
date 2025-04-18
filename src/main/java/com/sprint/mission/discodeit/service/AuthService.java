package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;

public interface AuthService {

  AuthLoginResponse login(LoginRequest request);
}
