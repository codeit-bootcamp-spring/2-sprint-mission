package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Create User_성공")
  void create_success() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("user@test.com", "user1234!", "유저1");

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "user.png",
        MediaType.IMAGE_PNG_VALUE,
        "fake-image-content".getBytes()
    );

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("유저1"))
        .andExpect(jsonPath("$.email").value("user@test.com"))
        .andExpect(jsonPath("$.profile.fileName").value("user.png"))
        .andExpect(jsonPath("$.profile.contentType").value("image/png"));
  }

  @Test
  @DisplayName("Create User_실패_이메일 형식 불일치")
  void create_failure_whenInvalidEmail() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("invalid-email", "pass123!", "뽀리");

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.details.email").value("올바른 이메일 형식이 아닙니다."));
  }

  @Test
  @DisplayName("findAll_성공")
  void findAllUsers_Success() throws Exception {
    // given
    UserCreateRequest userRequest1 = new UserCreateRequest(
        "user1",
        "user1@example.com",
        "Password1!"
    );

    UserCreateRequest userRequest2 = new UserCreateRequest(
        "user2",
        "user2@example.com",
        "Password1!"
    );

    userService.createUser(userRequest1, Optional.empty());
    userService.createUser(userRequest2, Optional.empty());

    // when, then
    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[0].email").value("user1@example.com"))
        .andExpect(jsonPath("$[1].username").value("user2"))
        .andExpect(jsonPath("$[1].email").value("user2@example.com"));
  }

  @Test
  @DisplayName("Update User_성공")
  void updateUser_Success() throws Exception {
    // given
    UserCreateRequest createRequest = new UserCreateRequest(
        "originaluser",
        "original@example.com",
        "Password1!"
    );
    UserDto createdUser = userService.createUser(createRequest, Optional.empty());
    UUID userId = createdUser.id();

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

    // when, then
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
        .andExpect(jsonPath("$.profile.fileName").value("updated-profile.jpg"));
  }

  @Test
  @DisplayName("Update User_실패_존재하지 않는 사용자")
  void updateUser_Failure_UserNotFound() throws Exception {
    // given
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

    // when, then
    mockMvc.perform(multipart("/api/users/{userId}", nonExistentUserId)
            .file(userUpdateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
  }

  @Test
  @DisplayName("Delete User_성공")
  void deleteUser_Success() throws Exception {
    // given
    UserCreateRequest createRequest = new UserCreateRequest(
        "deleteuser",
        "delete@example.com",
        "Password1!"
    );

    UserDto createdUser = userService.createUser(createRequest, Optional.empty());
    UUID userId = createdUser.id();

    // when, then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());

    // 삭제 확인
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + userId + "')]").doesNotExist());
  }

  @Test
  @DisplayName("Delete User_실패_존재하지 않는 사용자")
  void deleteUser_Failure_UserNotFound() throws Exception {
    // given
    UUID nonExistentUserId = UUID.randomUUID();

    // when, then
    mockMvc.perform(delete("/api/users/{userId}", nonExistentUserId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
  }

  @Test
  @DisplayName("Update User Status_성공")
  void updateUserStatus_Success() throws Exception {
    // given
    UserCreateRequest createRequest = new UserCreateRequest(
        "statususer",
        "status@example.com",
        "Password1!"
    );
    UserDto createdUser = userService.createUser(createRequest, Optional.empty());
    UUID userId = createdUser.id();

    Instant newLastActiveAt = Instant.now();
    UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest(
        newLastActiveAt
    );

    // when, then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastActiveAt").value(newLastActiveAt.toString()));
  }

  @Test
  @DisplayName("Update User Status_실패_존재하지 않는 사용자")
  void updateUserStatus_Failure_UserNotFound() throws Exception {
    // given
    UUID nonExistentUserId = UUID.randomUUID();
    UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest(
        Instant.now()
    );

    // when, then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", nonExistentUserId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusUpdateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
  }

}