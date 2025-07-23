package com.sprint.mission.discodeit.testpratice.security;

import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfigureMockMvc
class AuthorizationTest extends IntegrationTestSupport {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SessionRegistry sessionRegistry;
  @Autowired
  private AuthService authService;

  @AfterEach
  void tearDown() {
    for (Object principal : sessionRegistry.getAllPrincipals()) {
      List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
      for (SessionInformation session : sessions) {
        session.expireNow();
      }
    }

    userRepository.deleteAllInBatch();
  }

  @DisplayName("역할을 업데이트하면, 현재 유지하고 있는 세션이 종료됩니다.")
  @Test
  void updateRole() {
    // given
    String name = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    String hashedPassword = passwordEncoder.encode(password);
    User savedUser = userRepository.save(new User(name, "", hashedPassword, null));

    sessionRegistry.registerNewSession("mock-session-id", savedUser.getName());

    RoleUpdateRequest request = new RoleUpdateRequest(savedUser.getId(), Role.CHANNEL_MANAGER);

    // when
    authService.updateRole(request);

    // then
    List<SessionInformation> sessions = sessionRegistry.getAllSessions(savedUser.getName(), true);
    Assertions.assertThat(sessions.get(0).isExpired()).isTrue();
  }

}