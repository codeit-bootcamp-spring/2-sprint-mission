package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLogin;
import com.sprint.mission.discodeit.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthLogin dto);
}
