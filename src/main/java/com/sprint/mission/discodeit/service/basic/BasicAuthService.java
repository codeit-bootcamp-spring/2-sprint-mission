package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserService userService;
  @Value("${discodeit.admin.username}")
  private String adminUsername;

  @Value("${discodeit.admin.password}")
  private String adminPassword;

  @Value("${discodeit.admin.email}")
  private String adminEmail;

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

  @Transactional
  public void initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(adminUsername)) {
      log.warn("이미 어드민이 존재합니다.");
    }

    UserCreateRequest adminUser = new UserCreateRequest(adminUsername,adminEmail,adminPassword);
    UserDto adminDto = userService.create(adminUser, Optional.empty());

    User admin = userRepository.findById(adminDto.id())
            .orElseThrow(UserNotFoundException::new);
    admin.setRole(Role.ADMIN);
    userRepository.save(admin);

    log.info("어드민이 초기화되었습니다. {}", admin);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest, HttpServletRequest request) {
    log.debug("로그인 시도: username={}", loginRequest.username());
    
    String username = loginRequest.username();
    String password = loginRequest.password();

    Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
    );

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);

    HttpSession session = request.getSession(true);
    session.setAttribute("SPRING_SECURITY_CONTEXT", context);

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    return userMapper.toDto(user);
  }

  @Override
  public void logout(HttpServletRequest request) {
    log.info("로그아웃 처리 시작");

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    SecurityContextHolder.clearContext();

    log.info("로그아웃 완료");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
            .orElseThrow(UserNotFoundException::new);

    user.updateRole(request.newRole());

    System.out.println("=== SESSION DEBUG ===");
    System.out.println("Target user email: " + user.getEmail());

    sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal.equals(user.getEmail()))
            .forEach(principal -> {
              sessionRegistry.getAllSessions(principal, false)
                      .forEach(sessionInfo -> sessionInfo.expireNow());
            });

    return userMapper.toDto(user);
  }
}
