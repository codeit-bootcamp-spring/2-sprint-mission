package com.sprint.mission.discodeit.testpratice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.dto.LogInRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.testutil.ControllerTestSupport;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@AutoConfigureMockMvc
public class AuthenticationTest extends IntegrationTestSupport {

  @Autowired
  private MockMvcTester mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @AfterEach
  void tearDown() {
    userRepository.deleteAllInBatch();
  }

  @DisplayName("기본적인 인증을 구현합니다.")
  @Test
  void securityAuthTest() throws JsonProcessingException {
    // given
    String name = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    String hashedPassword = passwordEncoder.encode(password);
    User savedUser = userRepository.save(new User(name, "", hashedPassword, null));

    String logInUrl = "/api/auth/login";
    LogInRequest logInRequest = new LogInRequest(name, password);

    // when
    MvcTestResult result = mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    // then
    HttpSession session = result.getRequest().getSession(false);
    Assertions.assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT"))
        .extracting(
            "authentication.principal.username",
            "authentication.principal.password",
            "authentication.authenticated"
        )
        .containsExactlyInAnyOrder(name, hashedPassword, true);
  }

  @DisplayName("로그아웃하면 세션를 무효화 합니다.")
  @Test
  void logoutTest() throws JsonProcessingException {
    // given
    String name = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    String hashedPassword = passwordEncoder.encode(password);
    User savedUser = userRepository.save(new User(name, "", hashedPassword, null));

    String logInUrl = "/api/auth/login";
    LogInRequest logInRequest = new LogInRequest(name, password);

    MvcTestResult loginResult = mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

    // when
    mockMvc.post()
        .uri("/api/auth/logout")
        .session(session)
        .exchange();

    // then
    Assertions.assertThat(session.isInvalid())
        .isTrue();
  }

}
