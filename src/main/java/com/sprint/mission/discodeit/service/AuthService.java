package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserLoginDto;

public interface AuthService {
    UserDto login(UserLoginDto loginRequestUser);
}
