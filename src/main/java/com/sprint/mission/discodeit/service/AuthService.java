package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.user.LoginRequest;


public interface AuthService {
    User login(LoginRequest loginRequest);
}
