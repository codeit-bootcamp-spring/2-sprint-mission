package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.user.LoginRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public UserDto login(LoginRequest loginRequest) {
    User user = userService.findByUsername(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException("{username}에 해당하는 User가 없음"));
    if (!user.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않음");
    }

    return userMapper.toDto(user);
  }
}