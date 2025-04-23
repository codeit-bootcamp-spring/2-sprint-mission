package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + request.username()));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + user.getId()));

    if (!request.password().equals(user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    userStatus.updateByUserId();

    boolean online = userStatus.isOnline();
    return userMapper.toDto(user, online);
  }

}
