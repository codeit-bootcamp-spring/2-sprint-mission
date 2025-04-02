package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.LoginResponse;
import com.sprint.mission.discodeit.dto.RegisterResponse;

public interface AuthService {


    RegisterResponse register(CreateUserRequest request);

    LoginResponse login(LoginRequest request);
}
