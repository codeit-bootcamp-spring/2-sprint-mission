package com.sprint.mission.discodeit.security;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.ControllerTestSupport;
import com.sprint.mission.discodeit.IntegrationTestSupport;
import com.sprint.mission.discodeit.domain.auth.security.config.SecurityConfig;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@AutoConfigureMockMvc
public class CSRFTest extends IntegrationTestSupport {

  @MockitoBean
  private UserService userService;

  @Autowired
  private MockMvcTester mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("csrf 요청을 인증 하지 않습니다.")
  @Test
  void test_csrf() {
    // given
    String csrfUrl = "/api/auth/csrf-token";

    // when
    MockMvcTester.MockMvcRequestBuilder uri = mockMvc.get()
        .uri(csrfUrl);

    // then
    Assertions.assertThat(uri)
        .hasStatusOk();
  }

  @WithMockUser
  @DisplayName("Get 요청은 CSRF 토큰을 요구하지 않습니다.")
  @Test
  void test_ApiFormat() {
    // given
    String apiURL = "/api/users";

    // when
    MockMvcTester.MockMvcRequestBuilder heathCheck = mockMvc.get()
        .uri(apiURL);

    // then
    Assertions.assertThat(heathCheck)
        .hasStatusOk();
  }

  @DisplayName("Post 요청시, CSRF-token이 없으면 예외처리 입니다.")
  @Test
  void test_Post_NoneCsrfToken() {
    // given
    String postUrl = "/api/users";

    // when
    MockMvcTester.MockMvcRequestBuilder uri = mockMvc.post()
        .uri(postUrl);

    // then
    Assertions.assertThat(uri)
        .hasStatus4xxClientError();
  }

  @DisplayName("Post 요청시, CSRF-token이 있으면 정상 처리합니다.")
  @Test
  void test_CsrfToken() {
    // given
    String name = "이름";
    String email = "이메일@email.com";
    String password = "비밀번호비밀번호";

    String postUrl = "/api/users";
    UserCreateRequest userCreateRequest = new UserCreateRequest(name, email, password);
    MockMultipartFile multipartFile = new MockMultipartFile("userCreateRequest", null,
        MediaType.APPLICATION_JSON_VALUE,
        convertJsonRequest(userCreateRequest).getBytes());

    User user = new User(name, email, password, null);
    UserResult stubResult = UserResult.fromEntity(user, true);
    BDDMockito.given(userService.register(any(), any())).willReturn(stubResult);

    // when
    MvcTestResult result = mockMvc.post()
        .uri(postUrl)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .multipart()
        .file(multipartFile)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
        .exchange();

    // then
    Assertions.assertThat(result)
        .hasStatusOk();
  }

  private String convertJsonRequest(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JSON 직렬화 중 오류 발생", e);
    }
  }

}
