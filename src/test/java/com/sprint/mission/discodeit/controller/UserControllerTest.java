package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = JpaConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;
    private UserCreateRequest userCreateRequest;
    private UserDto userDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userCreateRequest = new UserCreateRequest("user", "test@naver.com", "123");
        userDto = new UserDto(userId, "user", "test@naver.com", null, true);
    }

    @Test
    @DisplayName("사용자 생성 성공 (프로필 이미지 없음)")
    void createUser_Success_NoProfile() throws Exception {
        given(userService.create(any(UserCreateRequest.class), eq(null))).willReturn(userDto);

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
            "userCreateRequest",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsString(userCreateRequest).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/users")
                .file(userCreateRequestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("user"))
            .andExpect(jsonPath("$.email").value("test@naver.com"))
            .andExpect(jsonPath("$.profile").doesNotExist())
            .andExpect(jsonPath("$.online").value(true));
    }

    @Test
    @DisplayName("사용자 생성 - 성공 (프로필 이미지 포함)")
    void createUser_Success_WithProfile() throws Exception {
        // given
        MockMultipartFile profileImage = new MockMultipartFile("profile", "profile.jpg",
            MediaType.IMAGE_JPEG_VALUE, "image_data".getBytes());
        given(userService.create(any(UserCreateRequest.class),
            any(MockMultipartFile.class))).willReturn(userDto);

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
            "userCreateRequest",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsString(userCreateRequest).getBytes(StandardCharsets.UTF_8)
        );

        // when & then
        mockMvc.perform(multipart("/api/users")
                .file(userCreateRequestPart)
                .file(profileImage)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    @DisplayName("사용자 생성 - 실패 (이메일 중복)")
    void createUser_Failure_EmailExists() throws Exception {

        given(userService.create(any(UserCreateRequest.class), eq(null)))
            .willThrow(new UserAlreadyExistException(
                com.sprint.mission.discodeit.exception.ErrorCode.USER_ALREADY_EXISTS,
                Collections.singletonMap("email", userCreateRequest.email())));

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
            "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(userCreateRequest));

        mockMvc.perform(multipart("/api/users")
                .file(userCreateRequestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(
                status().isConflict());
    }

    @Test
    @DisplayName("사용자 목록 조회 - 성공")
    void getAllUsers_Success() throws Exception {
        UserDto user2Dto = new UserDto(UUID.randomUUID(), "user2", "user2@naver.com", null,
            false);
        List<UserDto> users = Arrays.asList(userDto, user2Dto);
        given(userService.findAll()).willReturn(users);

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].username").value("user"))
            .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @DisplayName("사용자 삭제 - 성공")
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/users/{userId}", userId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자 삭제 - 실패 (사용자 없음)")
    void deleteUser_Failure_UserNotFound() throws Exception {

        doThrow(new UserNotFoundException(userId.toString())).when(userService).delete(userId);

        mockMvc.perform(delete("/api/users/{userId}", userId))
            .andExpect(
                status().isNotFound());
    }

} 