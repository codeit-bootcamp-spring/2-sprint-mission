package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
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
  public LoginDTO login(LoginParam loginParam) {
    User user = findUserByUsername(loginParam);
    checkPassword(user, loginParam);

    // getUserStatus로 영속성 컨텍스트에 가져옴 (LAZY) -> 변경감지로 userStatus가 자동으로 save됨
    user.getUserStatus().updateUserStatus();
    return authMapper.toLoginDTO(user);
  }

  private User findUserByUsername(LoginParam loginParam) {
    return userRepository.findByUsername(loginParam.username())
        .orElseThrow(() -> {
          logger.error("로그인 중 유저 찾기 실패: {}", loginParam.username());
          return RestExceptions.USER_NOT_FOUND;
        });
  }


  private void checkPassword(User user, LoginParam loginParam) {
    if (!BCrypt.checkpw(loginParam.password(), user.getPassword())) {
      throw RestExceptions.INVALID_PASSWORD;
    }
  }
}
