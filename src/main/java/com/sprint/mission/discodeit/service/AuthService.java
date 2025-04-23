package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.user.LoginRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;


public interface AuthService {

  UserDto login(LoginRequest loginRequest);
}
