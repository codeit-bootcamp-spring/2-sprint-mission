package com.sprint.mission.discodeit.core.auth.service;

import com.sprint.mission.discodeit.auth.CustomUserDetails;
import com.sprint.mission.discodeit.core.auth.dto.Role;
import com.sprint.mission.discodeit.core.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
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

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  @Value("${discodeit.admin.username}")
  private String username;
  @Value("${discodeit.admin.password}")
  private String password;
  @Value("${discodeit.admin.email}")
  private String email;

  private final JpaUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

  @Transactional
  @Override
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(email) || userRepository.existsByName(username)) {
      return null;
    }

    String encodedPassword = passwordEncoder.encode(password);
    User admin = User.create(username, email, encodedPassword, null);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    return UserDto.create(admin, true);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    user.updateRole(request.newRole());

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> ((CustomUserDetails) principal).getUserDto().id().equals(userId))
        .findFirst()
        .ifPresent(principal -> {
              List<SessionInformation> activeSessions = sessionRegistry.getAllSessions(principal,
                  false);
              activeSessions.forEach(SessionInformation::expireNow);
            }
        );

    return UserDto.create(user, true);
  }
}
