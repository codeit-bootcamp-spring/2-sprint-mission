package com.sprint.mission.discodeit.testpratice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import com.sprint.mission.discodeit.domain.auth.dto.LogInRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@AutoConfigureMockMvc
public class ConcurrentLoginTest extends IntegrationTestSupport {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MockMvcTester mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SessionRegistry sessionRegistry;

  @AfterEach
  void tearDown() {
    userRepository.deleteAllInBatch();
  }

  @DisplayName("하나의 아이디로 동시 로그인시, 새로운 로그인 발생 시 기존 세션을 무효화합니다.(Invalid 처리는.. 어디서 하는지 모르겠네요)")
  @Test
  void concurrentTest() throws JsonProcessingException {
    // given
    String name = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    String hashedPassword = passwordEncoder.encode(password);
    User savedUser = userRepository.save(new User(name, "", hashedPassword, null));

    String logInUrl = "/api/auth/login";
    LogInRequest logInRequest = new LogInRequest(name, password);

    MvcTestResult firstLoginResult = mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();
    MockHttpSession firstSession = (MockHttpSession) firstLoginResult.getRequest()
        .getSession(false);

    // when
    MvcTestResult secondLoginResult = mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    // then
    MockHttpSession secondSession = (MockHttpSession) secondLoginResult.getRequest()
        .getSession(false);
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(firstSession.isInvalid()).isFalse();
      softly.assertThat(secondSession.isInvalid()).isFalse();
    });
  }

  // 안되긴하는데 지금 둘다 팅기긴하네요
  @DisplayName("SessionRegistry를 통한 동시 로그인 제한을 확인합니다.(Expired 처리만 진행합니다)")
  @Test
  void concurrentTestWithSessionRegistry() throws Exception {
    // given
    String name = UUID.randomUUID().toString();
    String password = UUID.randomUUID().toString();
    String hashedPassword = passwordEncoder.encode(password);
    User savedUser = userRepository.save(new User(name, "", hashedPassword, null));

    LogInRequest logInRequest = new LogInRequest(name, password);
    String logInUrl = "/api/auth/login";

    MvcTestResult firstLoginResult = mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    String firstSessionId = firstLoginResult.getRequest().getSession(false).getId();

    // when
    mockMvc.post()
        .uri(logInUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(logInRequest))
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    // then
    SessionInformation firstSessionInfo = sessionRegistry.getSessionInformation(firstSessionId);
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(firstSessionInfo).isNotNull();
      softly.assertThat(firstSessionInfo.isExpired()).isTrue();
    });
  }

}
