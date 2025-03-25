package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserInfoDto;

public interface AuthService {
    UserInfoDto login(LoginRequestDto loginRequestDto);
}
