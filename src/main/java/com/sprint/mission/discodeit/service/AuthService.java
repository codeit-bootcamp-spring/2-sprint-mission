package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginRequestDTO login(LoginRequestDTO loginRequestDTO);
}
