package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.update.UserLoginRequestDTO;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    String loginUser(UserLoginRequestDTO userLoginDTO);
}
