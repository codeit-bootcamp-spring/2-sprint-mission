package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.auth.LoginFailedPasswordException;
import com.sprint.mission.discodeit.exception.auth.LoginFailedUsernameException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> {
              log.warn("로그인 실패 - username: {}", username);
              Map<String, Object> details = new HashMap<>();
              details.put("username", username);
              return new LoginFailedUsernameException(Instant.now(), ErrorCode.LOGIN_FAILED_USERNAME, details);
            });

    if (!user.getPassword().equals(password)) {
      log.warn("로그인 실패 - password");
      Map<String, Object> details = new HashMap<>();
      throw new LoginFailedPasswordException(Instant.now(), ErrorCode.LOGIN_FAILED_PASSWORD, details);
    }

    return userMapper.toDto(user);
  }
}
