package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(UserController.class)
class UserControllerTest {

  private static final String BASE_URL = "/api/users";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private UserStatusService userStatusService;

  @MockitoBean
  private BinaryContentService binaryContentService;

  @MockitoBean
  private UserMapper userMapper;

  @MockitoBean
  private JwtUtil jwtUtil;


  @DisplayName("Post base url - 성공")
  @Test
  void createUser_Success() throws Exception {
    // given
    CreateUserRequest request = new CreateUserRequest(
        "test@test.com",
        "test",
        "test"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        null,
        "application/json",
        new ObjectMapper().writeValueAsBytes(request)
    );

    MockMultipartFile profileImage = new MockMultipartFile(
        "profile",
        "profile.png",
        MediaType.IMAGE_PNG_VALUE,
        "test image".getBytes(StandardCharsets.UTF_8)
    );

    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "profile.png",
        1L,
        MediaType.IMAGE_PNG_VALUE
    );

    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "test",
        "test@test.com",
        profileDto,
        false
    );

    given(authService.register(any(CreateUserRequest.class), any(MultipartFile.class))).willReturn(
        userDto);

    // when
    // then
    mockMvc.perform(multipart(BASE_URL)
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("test"))
        .andExpect(jsonPath("$.email").value("test@test.com"))
        .andExpect(jsonPath("$.profile.filename").value("profile.png"))
        .andExpect(jsonPath("$.online").value(false));
  }

  @DisplayName("Post invalid request - 실패")
  @Test
  void createUser_InvalidRequest_Fail() throws Exception {
    // given
    CreateUserRequest request = new CreateUserRequest(
        null,
        "test",
        "test"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        null,
        "application/json",
        new ObjectMapper().writeValueAsBytes(request)
    );

    // when
    // then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("Patch /{userId} - 성공")
  @Test
  void updateUser_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    UpdateUserRequest request = new UpdateUserRequest(
        "newName",
        "new@test.com",
        "test"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userUpdateRequest",
        null,
        "application/json",
        new ObjectMapper().writeValueAsBytes(request)
    );

    MockMultipartFile profileImage = new MockMultipartFile(
        "profile",
        "newprofile.png",
        MediaType.IMAGE_PNG_VALUE,
        "test image".getBytes(StandardCharsets.UTF_8)
    );

    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "profile.png",
        1L,
        MediaType.IMAGE_PNG_VALUE
    );

    UserDto userDto = new UserDto(
        userId,
        "newName",
        "new@test.com",
        profileDto,
        false
    );

    given(userService.updateUser(any(UUID.class), any(UpdateUserRequest.class),
        any(MockMultipartFile.class))).willReturn(userDto);

    // when
    // then
    mockMvc.perform(multipart(BASE_URL + "/" + userId)
            .file(requestPart)
            .file(profileImage)
            .with(request1 -> {
              request1.setMethod("PATCH");
              return request1;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newName"))
        .andExpect(jsonPath("$.email").value("new@test.com"))
        .andExpect(jsonPath("$.profile.filename").value("profile.png"))
        .andExpect(jsonPath("$.online").value(false));
  }

  @DisplayName("DELETE /{userId} - 성공")
  @Test
  void deleteUser_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    // when
    // then
    mockMvc.perform(delete(BASE_URL + "/" + userId))
        .andExpect(status().isNoContent());
  }

  @DisplayName("patch /{userId}/userStatus - 성공")
  @Test
  void updateUserStatus_Success() throws Exception {
    UUID userId = UUID.randomUUID();
    Instant now = Instant.now();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(now);
    UserStatusDto responseDto = new UserStatusDto(UUID.randomUUID(), userId, now);

    given(userStatusService.update(eq(userId), any(UserStatusUpdateRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(patch(BASE_URL + "/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastActiveAt").value(now.toString()));
  }
}