package com.sprint.mission.discodeit.service.advance;

import com.sprint.mission.discodeit.dto.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import java.util.Optional;

public class AuthServiceImp implements AuthService {
    UserRepository userRepository;

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        return userRepository.loginAuth(userLoginRequest.username(), userLoginRequest.password())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }
}
