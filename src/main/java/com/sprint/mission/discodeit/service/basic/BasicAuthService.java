package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.user.LoginRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public UserDto login(LoginRequest loginRequest) {
    log.debug("로그인 시작: {}", loginRequest);
    User user = userService.findByUsername(loginRequest.username())
        .orElseThrow(InvalidCredentialsException::new);
    if (!user.getPassword().equals(loginRequest.password())) {
      throw new InvalidCredentialsException();
    }
    log.info("로그인 성공: id={}", user.getId());
    return userMapper.toDto(user);
  }
}