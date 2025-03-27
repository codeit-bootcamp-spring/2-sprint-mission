package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.AuthResponseDto;

public interface AuthService {
    AuthResponseDto login(AuthLoginRequestDto dto);
}
