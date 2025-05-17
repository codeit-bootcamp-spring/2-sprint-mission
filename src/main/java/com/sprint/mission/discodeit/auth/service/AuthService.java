package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.auth.dto.auth.LoginRequest;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
