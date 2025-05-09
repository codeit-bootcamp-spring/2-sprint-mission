package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.MaskingUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final AuthMapper authMapper;

  @Override
  @Transactional
  public LoginResult login(LoginCommand loginCommand) {
    User user = findUserByUsername(loginCommand);
    checkPassword(user, loginCommand);

    // getUserStatus로 영속성 컨텍스트에 가져옴 (LAZY) -> 변경감지로 userStatus가 자동으로 save됨
    user.getUserStatus().updateUserStatus();
    log.info("Login success: userStatus updated (userId = {})", user.getId());
    return authMapper.toLoginResult(user);
  }

  private User findUserByUsername(LoginCommand loginCommand) {
    return userRepository.findByUsername(loginCommand.username())
        .orElseThrow(() -> {
          String maskedUsername = MaskingUtil.maskUsername(loginCommand.username());
          log.warn("Login failed: user not found (username = {})", maskedUsername);
          return new UserNotFoundException(Map.of("username", maskedUsername));
        });
  }

  private void checkPassword(User user, LoginCommand loginCommand) {
    if (!BCrypt.checkpw(loginCommand.password(), user.getPassword())) {
      String maskedUsername = MaskingUtil.maskUsername(loginCommand.username());
      log.warn("Login failed: invalid password (username = {})", maskedUsername);
      throw new InvalidPasswordException(Map.of("username", maskedUsername));
    }
  }
}
