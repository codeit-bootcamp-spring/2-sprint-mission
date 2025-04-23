package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.dto.user.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    UserDto login(LoginForm form);
}
