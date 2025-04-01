package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginRequestDTO;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.exception.user.UserLoginFailedError;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepositoryPort userRepositoryPort;

  @CustomLogging
  @Override
  public void loginUser(UserLoginRequestDTO requestDTO) {
    List<User> list = userRepositoryPort.findAll();
    User user = CommonUtils.findByName(list, requestDTO.userName(), User::getName)
        .orElseThrow(() -> new UserNotFoundError("로그인 실패: 해당 유저를 찾지 못했습니다."));

    if (!BCrypt.checkpw(requestDTO.password(), user.getPassword())) {
      throw new UserLoginFailedError("로그인 실패: 비밀번호가 틀립니다.");
    }

  }
}
