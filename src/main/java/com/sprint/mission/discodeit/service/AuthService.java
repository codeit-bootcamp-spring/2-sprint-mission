package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserLoginRequestDTO;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    boolean loginUser(UserLoginRequestDTO userLoginDTO);
}
