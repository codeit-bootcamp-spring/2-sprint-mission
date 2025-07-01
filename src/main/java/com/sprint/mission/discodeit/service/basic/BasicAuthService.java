package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.controller.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

  @Value("${sprint.admin.username}")
  private String adminUsername;
  @Value("${sprint.admin.password}")
  private String adminPassword;
  @Value("${sprint.admin.email}")
  private String adminEmail;

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

  @Transactional
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(
        adminUsername)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(adminPassword);
    User admin = new User(adminUsername, adminEmail, encodedPassword, null);
    admin.updateRole(Role.ADMIN);

    userRepository.save(admin);

    UserDto adminDto = userMapper.toUserDto(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @Override
  @Transactional
  public UserDto updateRole(RoleUpdateRequest roleUpdateRequest) {
    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> {
          log.warn("User updateRole failed: user not found (userId: {})",
              roleUpdateRequest.userId());
          return new UserNotFoundException(Map.of("userId", roleUpdateRequest.userId()));
        });
    user.updateRole(roleUpdateRequest.newRole());
    expireUserSessions(user.getUsername());
    return userMapper.toUserDto(user);
  }

  private void expireUserSessions(String username) {
    sessionRegistry.getAllPrincipals().stream()
        .filter(UserDetails.class::isInstance)
        .map(UserDetails.class::cast)
        .filter(userDetails -> userDetails.getUsername().equals(username))
        .flatMap(userDetails -> sessionRegistry.getAllSessions(userDetails, false).stream())
        .forEach(SessionInformation::expireNow);
  }
}


