package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.Role;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto updateRole(RoleUpdateRequest roleUpdateRequest) {
    UUID userId = roleUpdateRequest.userId();
    Role newRole = roleUpdateRequest.newRole();
    User user = userRepository.findById(userId).orElseThrow(() -> {
      UserNotFoundException exception = UserNotFoundException.withId(userId);
      return exception;
    });

    user.updateRole(newRole);

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> ((CustomUserDetails) principal).getUserDto().id().equals(userId))
        .findFirst()
        .ifPresent(principal -> {
              List<SessionInformation> activeSessions = sessionRegistry.getAllSessions(principal,
                  false);
              log.info("Active sessions: {}", activeSessions.size());
              activeSessions.forEach(SessionInformation::expireNow);
            }
        );

    return userMapper.toDto(user);
  }
}
