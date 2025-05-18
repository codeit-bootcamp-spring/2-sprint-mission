package com.sprint.mission.discodeit.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("이름과 비밀번호를 사용해서 로그인 합니다.")
    @Test
    void login() {
        // given
        User user = new User("22", "logInUser@naver.com", "1111/111", null);
        UserResult stubResult = UserResult.fromEntity(user, true);
        LoginRequest loginRequest = new LoginRequest("22", "1111/111");
        BDDMockito.given(authService.login(any())).willReturn(stubResult);

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/auth/login")
                        .content(convertJsonRequest(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.online")
                .isEqualTo(true);
    }

    private String convertJsonRequest(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 중 오류 발생", e);
        }
    }

    @DisplayName("로그인시 이름을 입력하지 않으면, 예외를 반환합니다.")
    @NullAndEmptySource
    @ValueSource(strings = {"a", "1111111111/1111111111"})
    @ParameterizedTest
    void login_ValidateName(String username) {
        // given
        User user = new User("logInUser", "logInUserEmail@naver.com", "logInUserEmailPassword", null);
        UserResult stubResult = UserResult.fromEntity(user, true);
        BDDMockito.given(authService.login(any())).willReturn(stubResult);
        LoginRequest invalidRequest = new LoginRequest(
                username,
                "logInUserEmailPassword"
        );

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/auth/login")
                        .content(convertJsonRequest(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .exchange())
                .hasStatus4xxClientError();
    }

    @DisplayName("로그인시 이름을 입력하지 않으면, 예외를 반환합니다.")
    @NullAndEmptySource
    @ValueSource(strings = {"7777/77", "1111111111/1111111111/111111111"})
    @ParameterizedTest
    void login_ValidatePassword(String password) {
        // given
        User user = new User("111", "logInUserEmail@naver.com", "1111111111/1111111111/111111111", null);
        UserResult stubResult = UserResult.fromEntity(user, true);
        BDDMockito.given(authService.login(any())).willReturn(stubResult);
        LoginRequest invalidRequest = new LoginRequest(
                "111",
                password
        );

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/auth/login")
                        .content(convertJsonRequest(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .exchange())
                .hasStatus4xxClientError();
    }

}