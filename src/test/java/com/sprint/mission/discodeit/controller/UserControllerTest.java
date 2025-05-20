package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.handler.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserStatusService userStatusService;

    @Test
    @DisplayName("POST /api/users - 사용자 생성 성공")
    void createUser_success() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest("testuser", "password123",
            "test@example.com", null, null);
        UUID userId = UUID.randomUUID();
        UserDto responseDto = new UserDto(userId, "testuser", "test@example.com",
            null, true);

        when(userService.createUser(any(CreateUserRequest.class), any())).thenReturn(responseDto);

        MockMultipartFile jsonPart = new MockMultipartFile("userCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request));

        // when & then
        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.online").value(true));
    }

    @Test
    @DisplayName("POST /api/users - 중복 사용자 예외 발생")
    void createUser_duplicate_failure() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest("testuser", "password123",
            "test@example.com", null, null);

        when(userService.createUser(any(CreateUserRequest.class), any()))
            .thenThrow(new DuplicateUserException());

        MockMultipartFile jsonPart = new MockMultipartFile("userCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request));

        // when & then
        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER.getMessage()));
    }

    @Test
    @DisplayName("PATCH /api/users/{userId} - 사용자 수정 성공")
    void updateUser_success() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        UpdateUserRequest request = new UpdateUserRequest(userId, "newUsername",
            "newPassword", "newemail@example.com");

        UserDto responseDto = new UserDto(userId, "newUsername",
            "newemail@example.com", null, true);

        given(userService.updateUser(any(UpdateUserRequest.class), any())).willReturn(responseDto);

        MockMultipartFile jsonPart = new MockMultipartFile(
            "userUpdateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(request)
        );

        // when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                .file(jsonPart)
                .with(req -> {
                    req.setMethod("PATCH");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("newUsername"))
            .andExpect(jsonPath("$.email").value("newemail@example.com"));
    }

    @Test
    @DisplayName("DELETE /api/users/{userId} - 사용자 삭제 성공")
    void deleteUser_success() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
            .andExpect(status().isNoContent());
    }
}
