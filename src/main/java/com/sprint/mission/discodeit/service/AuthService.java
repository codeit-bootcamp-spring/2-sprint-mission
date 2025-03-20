package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.LoginRequestDto;

public interface AuthService {
    UserResponseDto login(LoginRequestDto loginRequest);
}
