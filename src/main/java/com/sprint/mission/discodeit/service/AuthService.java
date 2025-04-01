package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.LoginDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto login(LoginDto loginDto);
}
