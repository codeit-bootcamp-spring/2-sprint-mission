package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Value("${discodeit.admin.username}")
  private String adminUsername;
  @Value("${discodeit.admin.password}")
  private String adminPassword;
  @Value("${discodeit.admin.email}")
  private String adminEmail;

  @Transactional
  @Override
  public void initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(adminUsername)) {
      log.warn("이미 어드민이 존재합니다.");
      return;
    }

    String encodedPassword = passwordEncoder.encode(adminPassword);
    User admin = new User(
        adminUsername,
        adminEmail,
        encodedPassword,
        null,
        UserRole.ADMIN);

    UserStatus status = new UserStatus(admin, Instant.now());

    userRepository.save(admin);
    log.info("어드민이 초기화되었습니다. {}", adminUsername);

  }

  @Transactional
  @Override
  public UserDto updateRole(UserRoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));

    user.setRole(request.role());
    userRepository.save(user);

    jwtService.invalidateAllUserSessions(user.getId());
    log.info("User role updated and all sessions invalidated for user ID: {}", user.getId());

    return userMapper.toDto(user);
  }

}
