package com.sprint.mission.discodeit.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("이름, 이메일, 패스워드를 입력받아, 유저를 생성합니다.")
    @Test
    void register() {
        // given
        String name = "22";
        String email = UUID.randomUUID() + "@naver.com";
        String password = "1111/111";

        User user = new User(name, email, password, null);
        UserResult stubResult = UserResult.fromEntity(user, true);
        BDDMockito.given(userService.register(any(), any())).willReturn(stubResult);
        UserCreateRequest userCreateRequest = new UserCreateRequest(name, email, password);
        MockMultipartFile multipartFile = new MockMultipartFile("userCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                convertJsonRequest(userCreateRequest).getBytes());

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/users")
                        .multipart()
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.email")
                .isEqualTo(email);
    }


    @DisplayName("유효하지 않은 이름, 이메일, 패스워드 받으면, 예외를 반환합니다..")
    @MethodSource("invalidUserProvider")
    @ParameterizedTest
    void register_Validation(String name, String email, String password, String expectedMessage) {
        // given
        User user = new User(name, email, password, null);
        UserResult stubResult = UserResult.fromEntity(user, true);
        BDDMockito.given(userService.register(any(), any())).willReturn(stubResult);
        UserCreateRequest userCreateRequest = new UserCreateRequest(name, email, password);
        MockMultipartFile multipartFile = new MockMultipartFile("userCreateRequest", null, MediaType.APPLICATION_JSON_VALUE, convertJsonRequest(userCreateRequest).getBytes());

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/users")
                        .multipart()
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatus4xxClientError();
    }

    static Stream<Arguments> invalidUserProvider() {
        return Stream.of(
                Arguments.of("", "test@naver.com", "12345678", "아이디는 필수입니다."), // username blank
                Arguments.of("a", "test@naver.com", "12345678", "이름은 2~20자 이내여야 합니다."), // username too short
                Arguments.of("validname", "invalid-email", "12345678", "must be a well-formed email address"), // email invalid
                Arguments.of("validname", "test@naver.com", "", "아이디는 필수입니다."), // password blank
                Arguments.of("validname", "test@naver.com", "short", "비밀번호는 8~30자 이내여야 합니다.") // password too short
        );
    }

    private String convertJsonRequest(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 중 오류 발생", e);
        }
    }

    @DisplayName("유저 ID를 받아, 해당 유저를 반환합니다.")
    @Test
    void getById() {
        // given
        String name = "22";
        String email = UUID.randomUUID() + "@naver.com";
        String password = "1111/111";
        String userId = UUID.randomUUID().toString();

        User user = new User(name, email, password, null);
        UserResult stubResult = UserResult.fromEntity(user, false);
        BDDMockito.given(userService.getById(any())).willReturn(stubResult);

        // when & then
        Assertions.assertThat(mockMvc.get().uri("/api/users/{userId}", userId))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.email")
                .isEqualTo(email);
    }

    @DisplayName("유저 ID를 null을 받거나 받지 못하면, 404 예외를 반환 합니다.")
    @Test
    void getById_Exception() {
        // given
        String name = "22";
        String email = UUID.randomUUID() + "@naver.com";
        String password = "1111/111";

        User user = new User(name, email, password, null);
        UserResult stubResult = UserResult.fromEntity(user, false);
        BDDMockito.given(userService.getById(any())).willReturn(stubResult);

        // when & then
        Assertions.assertThat(mockMvc.get().uri("/api/users/"))
                .hasStatus4xxClientError();
    }

}