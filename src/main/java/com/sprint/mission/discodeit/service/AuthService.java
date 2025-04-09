package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    User login(LoginForm form);
}
