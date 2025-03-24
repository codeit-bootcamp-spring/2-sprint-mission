package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.AuthRequestDTO;
import com.sprint.mission.discodeit.DTO.AuthResponseDTO;
import org.springframework.stereotype.Service;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO authRequest);
}
