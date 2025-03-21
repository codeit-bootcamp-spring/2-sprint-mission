package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    AuthLoginResponseDto login(AuthLoginRequestDto dto);

}
