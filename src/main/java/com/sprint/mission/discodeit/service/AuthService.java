package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponseDto;

public interface AuthService {
    AuthLoginResponseDto login(AuthLoginRequestDto requestDto);
}
