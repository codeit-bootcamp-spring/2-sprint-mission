package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;

    @Test
    @DisplayName("사용자 생성 성공 테스트")
    void createUser_Success() throws Exception {
        //Given
        UserCreateRequest createRequest = new UserCreateRequest(
                "testuser",
                "test@example.com",
                "Password1!"
        );
        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createRequest)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test-image".getBytes()
        );

        UUID userId = UUID.randomUUID();
        BinaryContentDto profileDto = new BinaryContentDto(
                UUID.randomUUID(),
                "profile.jpg",
                12L,
                MediaType.IMAGE_JPEG_VALUE
        );

        UserDto createdUser = new UserDto(
                userId,
                "testuser",
                "test@example.com",
                profileDto,
                false
        );

        given(userService.create(any(UserCreateRequest.class), any(Optional.class)))
                .willReturn(createdUser);

        //when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userCreateRequestPart)
                        .file(profilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.profile.fileName").value("profile.jpg"))
                .andExpect(jsonPath("$.online").value(false));
    }

    @Test
    @DisplayName("사용자 생성 실패 테스트 - 유효하지 않은 요청")
    void createUser_Failure_InvalidRequest() throws Exception {
        //Given
        UserCreateRequest invalidRequest = new UserCreateRequest(
                "t", // 최소 길이 위반
                "invalid-email", // 이메일 형식 위반
                "short" // 비밀번호 정책 위반
        );

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(invalidRequest)
        );

        //when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userCreateRequestPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 조회 성공 테스트")
    void findAllUsers_Success() throws Exception {
        //given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        UserDto user1 = new UserDto(
                userId1,
                "user1",
                "user1@example.com",
                null,
                true
        );
        UserDto user2 = new UserDto(
                userId2,
                "user2",
                "user2@example.com",
                null,
                false
        );

        List<UserDto> users = List.of(user1, user2);

        given(userService.findAll()).willReturn(users);

        //when & then
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId1.toString()))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].online").value(true))
                .andExpect(jsonPath("$[1].id").value(userId2.toString()))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].online").value(false));
    }

    @Test
    @DisplayName("사용자 업데이트 성공 테스트")
    void updateUser_Success() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "updateduser",
                "updated@example.com",
                "UpdatedPassword1!"
        );

        MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
                "userUpdateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(updateRequest)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "updated-profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "updated-image".getBytes()
        );

        BinaryContentDto profileDto = new BinaryContentDto(
                UUID.randomUUID(),
                "updated-profile.jpg",
                14L,
                MediaType.IMAGE_JPEG_VALUE
        );

        UserDto updatedUser = new UserDto(
                userId,
                "updateduser",
                "updated@example.com",
                profileDto,
                true
        );

        given(userService.update(eq(userId), any(UserUpdateRequest.class), any(Optional.class)))
                .willReturn(updatedUser);

        //when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                        .file(userUpdateRequestPart)
                        .file(profilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.profile.fileName").value("updated-profile.jpg"))
                .andExpect(jsonPath("$.online").value(true));
    }

    @Test
    @DisplayName("사용자 업데이트 실패 테스트 - 존재하지 않는 사용자")
    void updateUser_Failure_UserNotFound() throws Exception {
        //given
        UUID nonExistentUserId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "updateduser",
                "updated@example.com",
                "UpdatedPassword1!"
        );

        MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
                "userUpdateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(updateRequest)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "updated-profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "updated-image".getBytes()
        );

        given(userService.update(eq(nonExistentUserId), any(UserUpdateRequest.class), any(Optional.class)))
                .willThrow(UserNotFoundException.withId(nonExistentUserId));

        //when & then
        mockMvc.perform(multipart("/api/users/{userId}", nonExistentUserId)
                        .file(userUpdateRequestPart)
                        .file(profilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUser_Success() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userService).delete(userId);

        //when & then
        mockMvc.perform(delete("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자 삭제 실패 테스트 - 존재하지 않는 사용자")
    void deleteUser_Failure_UserNotFound() throws Exception {
        //given
        UUID nonExistentUserId = UUID.randomUUID();
        willThrow(UserNotFoundException.withId(nonExistentUserId))
                .given(userService).delete(nonExistentUserId);

        //when & then
        mockMvc.perform(delete("/api/users/{userId}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 상태 업데이트 성공 테스트")
    void updateUserStatus_Success() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        Instant lastActiveAt = Instant.now();

        UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(lastActiveAt);
        UserStatusDto updatedStatus = new UserStatusDto(statusId, userId, lastActiveAt);

        given(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
                .willReturn(updatedStatus);

        //When & then
        mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(updatedStatus)));
    }

    @Test
    @DisplayName("사용자 상태 업데이트 실패 테스트 - 존재하지 않는 사용자 상태")
    void updataeUserStatus_Failure_userStatusNotFound() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Instant lastActiveAt = Instant.now();

        UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(lastActiveAt);

        given(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
                .willThrow(UserNotFoundException.withId(userId));

        //when & then
        mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}
