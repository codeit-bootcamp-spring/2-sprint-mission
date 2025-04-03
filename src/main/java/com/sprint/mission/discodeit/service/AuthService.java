package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.LoginRequest;
import com.sprint.mission.discodeit.entity._User;


public interface AuthService {

  _User login(LoginRequest loginRequest);
}
