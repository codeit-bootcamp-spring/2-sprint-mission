package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.user.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.event.RoleChangedEvent;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  @Value("${discodeit.admin.username}")
  private String username;
  @Value("${discodeit.admin.password}")
  private String password;
  @Value("${discodeit.admin.email}")
  private String email;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  @Override
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(password);
    User admin = new User(username, email, encodedPassword, null);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    UserDto adminDto = UserDto.from(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    Role oldRole = user.getRole();
    user.updateRole(request.newRole());

    // 권한이 변경된 경우에만 알림 발행
    if (!oldRole.equals(request.newRole())) {
      eventPublisher.publishEvent(new RoleChangedEvent(userId, oldRole, request.newRole()));
    }

    jwtService.invalidateJwtSession(user.getId());
    return UserDto.from(user);
  }
}
