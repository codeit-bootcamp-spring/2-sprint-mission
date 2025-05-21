package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.auth.dto.LoginRequest;

public interface AuthService {
    UserResult login(LoginRequest loginRequestUser);
}
