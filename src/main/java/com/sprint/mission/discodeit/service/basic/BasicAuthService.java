package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.PasswordMismatchException;
import com.sprint.mission.discodeit.exception.auth.UserNameNotExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
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

    log.debug("유저 조회: username={}", username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> {
              log.warn("유저 조회 실패: username={}", username);
              return new UserNameNotExistsException(username);
            });

    if (!user.getPassword().equals(password)) {
      throw new PasswordMismatchException(password);
    }

    return userMapper.toDto(user);
  }
}
