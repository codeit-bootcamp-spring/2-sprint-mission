package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginRequestDTO;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

  void loginUser(UserLoginRequestDTO userLoginDTO);
}
