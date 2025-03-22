package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserLoginDto;

public interface AuthService {
    UserDto login(UserLoginDto loginRequestUser);
}
