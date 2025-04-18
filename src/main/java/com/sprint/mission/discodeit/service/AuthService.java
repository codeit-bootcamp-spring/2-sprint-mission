package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;

public interface AuthService {

  AuthLoginDto login(LoginRequest request);
}
