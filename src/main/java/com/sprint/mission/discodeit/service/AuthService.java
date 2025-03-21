package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.application.user.UserLoginDto;

public interface AuthService {
    UserDto login(UserLoginDto loginRequestUser);
}
