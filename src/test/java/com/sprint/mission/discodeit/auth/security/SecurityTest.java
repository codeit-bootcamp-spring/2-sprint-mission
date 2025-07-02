package com.sprint.mission.discodeit.auth.security;

import com.sprint.mission.discodeit.ControllerTestSupport;
import com.sprint.mission.discodeit.common.config.SecurityConfig;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@Import(SecurityConfig.class)
public class SecurityTest extends ControllerTestSupport {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;

    @DisplayName("/api/를 포함하지 않는 모든 URL 에 인증을 수행하지 않는다")
    @Test
    void test_NoneApiFormat() {
        // given
        String noneAPIURL = "/assets/index-kQJbKSsj.css";

        // when
        MockMvcTester.MockMvcRequestBuilder heathCheck = mockMvc.get()
                .uri(noneAPIURL);

        // then
        Assertions.assertThat(heathCheck)
                .hasStatusOk();
    }

    @DisplayName("/api/를 포함한 URL 에 대해서 요청을 수행합니다.")
    @Test
    void test_ApiFormat() {
        // given
        String apiURL = "/api/users";

        // when
        MockMvcTester.MockMvcRequestBuilder heathCheck = mockMvc.get()
                .uri(apiURL);

        // then
        Assertions.assertThat(heathCheck)
                .hasStatus4xxClientError();
    }

}
