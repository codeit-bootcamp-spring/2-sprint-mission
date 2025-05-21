package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
import java.util.NoSuchElementException;
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

    log.info("Attempting login for user: {}", username);

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.error("User with username {} not found", username);
          return UserException.usernameNotFound(Map.of("userName", username));
        });

    if (!user.getPassword().equals(password)) {
      log.error("Invalid password attempt: {}", username);
      throw AuthException.invalidPassword(Map.of("password", password));
    }

    log.info("User {} logged in successfully", username);
    return userMapper.toDto(user);
  }
}
