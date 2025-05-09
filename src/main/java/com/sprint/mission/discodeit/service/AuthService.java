package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.service.user.UserResult;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
