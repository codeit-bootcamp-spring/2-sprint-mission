package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.InvalidCredentialsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .filter(u -> u.getPassword().equals(request.password()))
        .orElseThrow(() -> new InvalidCredentialsException("로그인 실패"));

    userStatusService.updateByUserId(user.getId(), Instant.now());

    return userMapper.toResponse(user);
  }
}
