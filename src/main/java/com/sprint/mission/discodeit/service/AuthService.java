package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;

public interface AuthService {
    UserResponseDTO login(LoginRequestDTO dto);
}
