package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.NotificationType;
import com.sprint.mission.discodeit.domain.Role;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
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

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ApplicationEventPublisher eventPublisher;

  @Value("${account.admin.username}")
  private String adminUsername;
  @Value("${account.admin.password}")
  private String adminPassword;
  @Value("${account.admin.email}")
  private String adminEmail;

  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public UserDto updateUserRole(RoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.byId(request.userId()));
    Role previousRole = user.getRole();

    user.updateRole(request.newRole());
    jwtService.invalidateJwtSession(user.getId());

    eventPublisher.publishEvent(new NotificationEvent(
        request.userId(),
        NotificationType.ROLE_CHANGED,
        request.userId(),
        Map.of(
            "previousRole", previousRole.toString(),
            "newRole", request.newRole().toString()
        )
    ));

    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(
        adminUsername)) {
      log.warn("이미 존재하는 Admin 계정입니다.");
      return null;
    }

    User admin = User.create(adminUsername, adminEmail, passwordEncoder.encode(adminPassword),
        null);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    log.info("Admin 계정 초기화 완료: username={}", adminUsername);
    return userMapper.toDto(admin);
  }
}
