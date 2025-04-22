package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final AuthMapper authMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  @Transactional
  public LoginResult login(LoginCommand loginCommand) {
    User user = findUserByUsername(loginCommand);
    checkPassword(user, loginCommand);

    // getUserStatus로 영속성 컨텍스트에 가져옴 (LAZY) -> 변경감지로 userStatus가 자동으로 save됨
    user.getUserStatus().updateUserStatus();
    return authMapper.toLoginResult(user);
  }

  private User findUserByUsername(LoginCommand loginCommand) {
    return userRepository.findByUsername(loginCommand.username())
        .orElseThrow(() -> {
          logger.error("로그인 중 유저 찾기 실패: {}", loginCommand.username());
          return RestExceptions.USER_NOT_FOUND;
        });
  }


  private void checkPassword(User user, LoginCommand loginCommand) {
    if (!BCrypt.checkpw(loginCommand.password(), user.getPassword())) {
      throw RestExceptions.INVALID_PASSWORD;
    }
  }
}
