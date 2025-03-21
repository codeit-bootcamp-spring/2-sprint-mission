package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserLoginDto;

public interface AuthService {
    UserDto login(UserLoginDto loginRequestUser);
}
