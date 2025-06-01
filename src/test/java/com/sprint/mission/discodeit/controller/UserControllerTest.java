package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import com.sprint.mission.discodeit.mapper.UserStatusMapperImpl;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(UserController.class)
@Import({UserMapperImpl.class, BinaryContentMapperImpl.class, UserStatusMapperImpl.class})
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private BinaryContentMapper binaryContentMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("프로필 이미지 포함 회원 생성 성공")
  void createUser_with_validProfile_success() throws Exception {
    // Given
    CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
        "testUser", "ValidPass123!", "test@example.com"
    );

    // @RequestPart는 multipart/form-data 요청에서 찾기 때문에, multipart의 일부로 보내줘야함
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDTO)
    );

    byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "profile.png", MediaType.IMAGE_PNG_VALUE, pngHeader
    );

    BinaryContent binaryContent = BinaryContent.builder()
        .size(profileImage.getSize())
        .contentType(profileImage.getContentType())
        .filename(profileImage.getOriginalFilename())
        .build();

    CreateUserResult createUserResult = new CreateUserResult(UUID.randomUUID(),
        binaryContentMapper.toFindBinaryContentResult(binaryContent), requestDTO.username(),
        requestDTO.email(), true);

    given(userService.create(userMapper.toCreateUserCommand(requestDTO), profileImage)).willReturn(
        createUserResult);

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(createUserResult.id().toString()))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("프로필 이미지가 잘못된 타입일 때, 회원 생성 실패")
  void createUser_with_InValidProfile_type_failed() throws Exception {
    // Given
    CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
        "testUser", "ValidPass123!", "test@example.com"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDTO)
    );

    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "profile.png", MediaType.IMAGE_PNG_VALUE, "invalid_png".getBytes()
    );

    BinaryContent binaryContent = BinaryContent.builder()
        .size(profileImage.getSize())
        .contentType(profileImage.getContentType())
        .filename(profileImage.getOriginalFilename())
        .build();

    CreateUserResult createUserResult = new CreateUserResult(UUID.randomUUID(),
        binaryContentMapper.toFindBinaryContentResult(binaryContent), requestDTO.username(),
        requestDTO.email(), true);

    given(userService.create(userMapper.toCreateUserCommand(requestDTO), profileImage)).willReturn(
        createUserResult);

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().is(ErrorCode.UNSUPPORTED_PROFILE_MEDIA_TYPE.getStatus()));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 회원 생성 실패")
  void createUser_with_InValidParameter_failed() throws Exception {
    // Given
    CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
        "", "ValidPass123!", "test@example.com"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDTO)
    );

    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "profile.png", MediaType.APPLICATION_JSON_VALUE, "just_plain_text".getBytes()
    );

    BinaryContent binaryContent = BinaryContent.builder()
        .size(profileImage.getSize())
        .contentType(profileImage.getContentType())
        .filename(profileImage.getOriginalFilename())
        .build();

    CreateUserResult createUserResult = new CreateUserResult(UUID.randomUUID(),
        binaryContentMapper.toFindBinaryContentResult(binaryContent), requestDTO.username(),
        requestDTO.email(), true);

    given(userService.create(userMapper.toCreateUserCommand(requestDTO), profileImage)).willReturn(
        createUserResult);

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("프로필 이미지 포함 회원 수정 성공")
  void updateUser_withProfile_success() throws Exception {
    // Given
    UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(
        "newUser", "new@example.com", "newPass123!"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDTO)
    );

    byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "updateProfile.png", MediaType.IMAGE_PNG_VALUE, pngHeader
    );

    BinaryContent binaryContent = BinaryContent.builder()
        .size(profileImage.getSize())
        .contentType(profileImage.getContentType())
        .filename(profileImage.getOriginalFilename())
        .build();

    UUID userId = UUID.randomUUID();

    UpdateUserResult updateUserResult = new UpdateUserResult(userId, Instant.now(),
        binaryContentMapper.toFindBinaryContentResult(binaryContent), requestDTO.newUsername(),
        requestDTO.newEmail(), true);

    given(userService.update(userId, userMapper.toUpdateUserCommand(requestDTO),
        profileImage)).willReturn(
        updateUserResult);

    // When & Then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("newUser"))
        .andExpect(jsonPath("$.email").value("new@example.com"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 회원 수정 실패")
  void updateUser_withInValid_parameter_failed() throws Exception {
    // Given
    UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(
        "", "new@example.com", "newPass123!"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDTO)
    );

    byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "updateProfile.png", MediaType.IMAGE_PNG_VALUE, pngHeader
    );

    BinaryContent binaryContent = BinaryContent.builder()
        .size(profileImage.getSize())
        .contentType(profileImage.getContentType())
        .filename(profileImage.getOriginalFilename())
        .build();

    UUID userId = UUID.randomUUID();

    UpdateUserResult updateUserResult = new UpdateUserResult(userId, Instant.now(),
        binaryContentMapper.toFindBinaryContentResult(binaryContent), requestDTO.newUsername(),
        requestDTO.newEmail(), true);

    given(userService.update(userId, userMapper.toUpdateUserCommand(requestDTO),
        profileImage)).willReturn(
        updateUserResult);

    // When & Then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .file(requestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }
}