package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface AuthService {
    void login(LoginRequestDto loginRequestDto);
}
