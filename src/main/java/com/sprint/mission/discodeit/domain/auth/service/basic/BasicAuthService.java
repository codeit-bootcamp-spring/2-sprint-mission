package com.sprint.mission.discodeit.domain.auth.service.basic;

import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import com.sprint.mission.discodeit.domain.notification.event.event.RoleChangeNotificationEvent;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.repository.JwtSessionRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtSessionRepository jwtSessionRepository;
  private final ApplicationEventPublisher eventPublisher;

  private final String adminUsername = "adminUsername";
  private final String adminPassword = "adminPassword";
  private final String adminEmail = "adminEmail";

  @Transactional
  @Override
  public UserResult initAdmin() {
    if (userRepository.existsUserByEmail(adminEmail) || userRepository.existsUserByName(
        adminUsername)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(adminPassword);
    User admin = new User(adminUsername, adminEmail, encodedPassword, null);
    admin.updateRole(Role.ADMIN);

    User savedUser = userRepository.save(admin);

    return UserResult.fromEntity(savedUser, false);
  }

  @Transactional
  @Override
  public UserResult updateRole(RoleUpdateRequest roleUpdateRequest) {
    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(Map.of()));
    user.updateRole(roleUpdateRequest.newRole());
    User savedUser = userRepository.save(user);

    publishRoleChangeEvent(savedUser);

    jwtSessionRepository.deleteById(savedUser.getId());

    return UserResult.fromEntity(savedUser, false);
  }

  private void publishRoleChangeEvent(User user) {
    RoleChangeNotificationEvent roleChangeNotificationEvent = new RoleChangeNotificationEvent(
        user.getId(),
        user.getName()
    );
    eventPublisher.publishEvent(roleChangeNotificationEvent);
  }

}
