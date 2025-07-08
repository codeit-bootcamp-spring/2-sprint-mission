package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@AutoConfigureMockMvc
public class SecurityTest extends IntegrationTestSupport {

  @Autowired
  private MockMvcTester mockMvc;

  @DisplayName("/api/를 포함하지 않는 모든 URL 에 인증을 수행하지 않는다")
  @Test
  void test_NoneApiFormat() {
    // given
    String noneAPIURL = "/assets/index-kQJbKSsj.css";

    // when
    MvcTestResult result = mockMvc.get()
        .uri(noneAPIURL)
        .exchange();

    // then
    Assertions.assertThat(result)
        .hasStatusOk();
  }

  @DisplayName("/api/를 포함한 URL 에 대해서 요청을 수행합니다.")
  @Test
  void test_ApiFormat() {
    // given
    String apiURL = "/api/users";

    // when
    MvcTestResult result = mockMvc.get()
        .uri(apiURL)
        .exchange();

    // then
    Assertions.assertThat(result)
        .hasStatus4xxClientError();
  }

}
