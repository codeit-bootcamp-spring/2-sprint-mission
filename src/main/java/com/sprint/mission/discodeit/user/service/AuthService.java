package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.auth.LoginRequest;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
