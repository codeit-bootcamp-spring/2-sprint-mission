package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.util.JsonConvertor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private AuthService authService;

    @Test
    void login() {
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null);
        UserResult stubResult = UserResult.fromEntity(user, true);

        when(authService.login(any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/login")
                .content(JsonConvertor.asString(new LoginRequest(LOGIN_USER.getName(), LOGIN_USER.getPassword())))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.isLogin")
                .isEqualTo(true);
    }
}