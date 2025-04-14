package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.user.LoginRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;


public interface AuthService {

  UserResponse login(LoginRequest loginRequest);
}
