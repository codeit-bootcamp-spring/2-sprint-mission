package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.dto.service.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
  public void createUser_Success() throws Exception {
    UserCreateRequest request = new UserCreateRequest("user", "user@gmail.com", "user1234");
    UUID userId = UUID.randomUUID();
    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "human.jpg",
        1024L,
        MediaType.IMAGE_JPEG_VALUE
    );

    UserDto expected = new UserDto(userId, "user", "user@gmail.com", profileDto, false);

    given(userService.create(eq(request), any(BinaryContentCreateRequest.class)))
        .willReturn(expected);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );
    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "human.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );

    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("user"))
        .andExpect(jsonPath("$.email").value("user@gmail.com"))
        .andExpect(jsonPath("$.profile.fileName").value("human.jpg"))
        .andExpect(jsonPath("$.online").value(false));
  }

  @Test
  void createUser_Failure_InvalidUserCreateRequest() throws Exception {
    UserCreateRequest request = new UserCreateRequest(
        "s",
        "eee@",
        "11"
    );

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUser_Success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest("user", "user@gmail.com", "user1234");
    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "human.jpg",
        1024L,
        MediaType.IMAGE_JPEG_VALUE
    );
    UserDto updatedUser = new UserDto(
        userId, "user11", "user11@gmail.com", profileDto, true);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );
    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "human.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );

    given(userService.update(eq(userId), any(UserUpdateRequest.class),
        any(BinaryContentCreateRequest.class)))
        .willReturn(updatedUser);

    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(jsonPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("user11"))
        .andExpect(jsonPath("$.email").value("user11@gmail.com"))
        .andExpect(jsonPath("$.profile.fileName").value("human.jpg"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  void updateUser_Failure_UserNotFound() throws Exception {
    UUID invalidUserId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest("user", "user@gmail.com", "user1234");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );
    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "human.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );

    UserNotFoundException ex = new UserNotFoundException().notFoundWithId(invalidUserId);
    given(userService.update(eq(invalidUserId), any(UserUpdateRequest.class),
        any(BinaryContentCreateRequest.class))).willThrow(ex);

    mockMvc.perform(multipart("/api/users/{userId}", invalidUserId)
            .file(jsonPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound());
  }
  
  @Test
  void deleteUser_Success() throws Exception {
    UUID userId = UUID.randomUUID();
    willDoNothing().given(userService).delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteUser_Failure_UserNotFound() throws Exception {
    UUID invalidChannelId = UUID.randomUUID();
    UserNotFoundException ex = new UserNotFoundException().notFoundWithId(invalidChannelId);
    willThrow(ex).given(userService).delete(invalidChannelId);

    mockMvc.perform(delete("/api/users/{userId}", invalidChannelId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateUserStatus_Success() throws Exception {
    UUID userStatusId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Instant lastActiveAt = Instant.now();

    UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(lastActiveAt);
    UserStatusDto updatedStatus = new UserStatusDto(userStatusId, userId, lastActiveAt);

    given(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
        .willReturn(updatedStatus);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userStatusId.toString()))
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(content().json(objectMapper.writeValueAsString(updatedStatus)));
  }

  @Test
  void updateUserStatus_Failure_UserNotFound() throws Exception {
    UUID userId = UUID.randomUUID();
    Instant lastActiveAt = Instant.now();

    UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(lastActiveAt);
    UserNotFoundException ex = new UserNotFoundException()
        .notFoundWithId(userId);

    given(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
        .willThrow(ex);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }
}
