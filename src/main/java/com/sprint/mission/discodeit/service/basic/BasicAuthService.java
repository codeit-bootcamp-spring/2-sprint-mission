package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto register(CreateUserRequest request) {
    log.info("Registering user: {}", request);
    String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.email())) {
      log.warn("Email already exists: {}", request.email());
      throw new IllegalArgumentException("이미 존재하는 Email입니다");
    }
    if (userRepository.existsByUsername(request.username())) {
      log.warn("Username already exists: {}", request.username());
      throw new IllegalArgumentException("이미 존재하는 Username입니다");
    }

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password(hashedPassword)
        .build();
    userRepository.save(user);

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.save(userStatus);
    user.setStatus(userStatus);

    log.info("Registered user successfully: {}", user.getId());
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public UserDto login(LoginRequest request) {
    log.info("Login user: {}", request);
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(NoSuchElementException::new);

    if (!BCrypt.checkpw(request.password(), user.getPassword())) {
      log.warn("Wrong password: {}", request.password());
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    userStatusRepository.findByUserId(user.getId())
        .ifPresent(UserStatus::updateLastActiveAt);

    log.info("Login user successfully: {}", user.getId());
    return userMapper.toDto(user);
  }
}
