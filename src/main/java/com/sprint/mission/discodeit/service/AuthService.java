package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
