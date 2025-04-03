package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;


public interface AuthService {

  User login(LoginRequest loginRequest);
}
