package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.DuplicateUserEmail;
import com.sprint.mission.discodeit.exception.user.DuplicateUserUserName;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;

    @Test
    @DisplayName("사용자 생성 성공 - 201 Created")
    void createUser_success() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("user1", "user1@email.com", "pw");
        UserDto response = new UserDto(UUID.randomUUID(), "user1", "user1@email.com", null, false);

        given(userService.create(any(), any())).willReturn(response);

        MockMultipartFile userRequest = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@email.com"));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 중복 이메일")
    void createUser_fail_duplicateEmail() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("user1", "dup@email.com", "pw");
        given(userService.create(any(), any())).willThrow(new DuplicateUserEmail());

        MockMultipartFile userRequest = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 수정 성공 - 200 OK")
    void updateUser_success() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("newUser", "new@email.com", "newPw");
        UserDto response = new UserDto(userId, "newUser", "new@email.com", null, false);

        given(userService.update(eq(userId), any(), any())).willReturn(response);

        MockMultipartFile userRequest = new MockMultipartFile(
                "userUpdateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                        .file(userRequest)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    @DisplayName("사용자 수정 실패 - 사용자 없음 (404 Not Found)")
    void updateUser_fail_userNotFound() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("newUser", "new@email.com", "newPw");
        given(userService.update(eq(userId), any(), any())).willThrow(new UserNotFoundException());

        MockMultipartFile userRequest = new MockMultipartFile(
                "userUpdateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                        .file(userRequest)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 삭제 성공 - 204 No Content")
    void deleteUser_success() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userService).delete(userId);

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 사용자 없음 (404 Not Found)")
    void deleteUser_fail_userNotFound() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        willThrow(new UserNotFoundException()).given(userService).delete(userId);

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }
}

