package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResult;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
