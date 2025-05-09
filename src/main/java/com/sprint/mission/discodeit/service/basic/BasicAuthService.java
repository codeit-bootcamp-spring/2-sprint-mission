package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.UserMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    log.info("Login attempt: username={}", username);

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
              log.warn("Login failed username={}", username);
              return new NoSuchElementException("User with username " + username + " not found");
            });

    if (!user.getPassword().equals(password)) {
      log.warn("Login failed username={}", username);
      throw new IllegalArgumentException("Wrong password");
    }

    UserDto userDto = userMapper.toDto(user);
    log.info("Login successful: username={}", username);
    return userDto;
  }
}
