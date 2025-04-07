package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;

public interface AuthService {

  AuthLoginResponse login(AuthLoginRequest request);
}
