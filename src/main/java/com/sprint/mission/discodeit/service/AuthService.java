package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
