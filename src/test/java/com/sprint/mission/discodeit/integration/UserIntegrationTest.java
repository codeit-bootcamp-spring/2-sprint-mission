package com.sprint.mission.discodeit.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.dto.service.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
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
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  private final String username = "user";
  private final String email = "user@gmail.com";
  private final String filename = "human.jpg";

  private UserCreateRequest createRequest(String username, String email) {
    return new UserCreateRequest(username, email, "password");
  }

  private MockMultipartFile createProfileMultipartFile() throws IOException {
    return new MockMultipartFile(
        "profile",
        filename,
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );
  }

  @Test
  void createUser_Success() throws Exception {
    UserCreateRequest request = createRequest(username, email);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile profilePart = createProfileMultipartFile();

    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.profile.fileName").value(filename))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  void createUser_Failure_InvalidRequest() throws Exception {
    UserCreateRequest request = createRequest("t", "ss@");

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
    UserCreateRequest createRequest = createRequest(username, email);

    UserDto userDto = userService.create(createRequest, null);
    UUID id = userDto.id();

    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "user11", "user11@gmail.com", "updatedUser");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );

    MockMultipartFile profilePart = createProfileMultipartFile();

    mockMvc.perform(multipart("/api/users/{userId}", id)
            .file(jsonPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.username").value("user11"))
        .andExpect(jsonPath("$.email").value("user11@gmail.com"))
        .andExpect(jsonPath("$.profile.fileName").value(filename));
  }

  @Test
  void updateUser_Failure_UserNotFound() throws Exception {
    UUID id = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "user11", "user11@gmail.com", "updatedUser");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );

    mockMvc.perform(multipart("/api/users/{userId}", id)
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteUser_Success() throws Exception {
    UserCreateRequest createRequest = createRequest(username, email);
    UserDto userDto = userService.create(createRequest, null);
    UUID id = userDto.id();

    mockMvc.perform(delete("/api/users/{userId}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteUser_Failure_UserNotFound() throws Exception {
    mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void findAllUsers_Success() throws Exception {
    UserCreateRequest userRequest1 = createRequest(username, email);
    UserCreateRequest userRequest2 = createRequest("user2", "user2@gmail.com");
    userService.create(userRequest1, null);
    userService.create(userRequest2, null);

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value(username))
        .andExpect(jsonPath("$[0].email").value(email))
        .andExpect(jsonPath("$[1].username").value("user2"))
        .andExpect(jsonPath("$[1].email").value("user2@gmail.com"));
  }

  @Test
  void updateStatus_Success() throws Exception {
    UserCreateRequest createRequest = createRequest(username, email);

    UserDto userDto = userService.create(createRequest, null);
    UUID id = userDto.id();

    Instant newLastActiveAt = Instant.now();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastActiveAt").value(newLastActiveAt.toString()));
  }

  @Test
  void updateUserStatus_Failure_UserNotFound() throws Exception {
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());

    mockMvc.perform(patch("/api/users/{userId}/userStatus", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }
}
