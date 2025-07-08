package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  @Value("${security.admin.email}") String email;
  @Value("${security.admin.username}") String username;
  @Value("${security.admin.password}") String password;

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

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

    UserDto adminDto = userMapper.toDto(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public UserDto updateRole(UserRoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    user.updateRole(request.newRole());

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> ((CustomUserDetails) principal).getUserDto().id().equals(userId))
        .findFirst()
        .ifPresent(principal -> {
              List<SessionInformation> activeSessions = sessionRegistry.getAllSessions(principal,
                  false);
              log.debug("Active sessions: {}", activeSessions.size());
              activeSessions.forEach(SessionInformation::expireNow);
            }
        );

    return userMapper.toDto(user);
  }
}
