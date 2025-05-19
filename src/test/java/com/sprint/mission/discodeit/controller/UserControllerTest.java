package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.Map;
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
class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserStatusService userStatusService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("사용자 생성 요청 성공 - multipart/form-data")
  void createUser_success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserCreateRequest request = new UserCreateRequest("testuser", "test@example.com", "password123");

    // multipart/form-data의 "JSON part" 준비 (MultipartFile로 들어온 데이터를 UserCreateRequest로 매핑)
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    // multipart/form-data의 "file part" 준비
    MockMultipartFile profileFile = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        new byte[]{1, 2, 3}
    );

    UserDto mockUserDto = new UserDto(
        userId,
        request.username(),
        request.email(),
        new BinaryContentDto(UUID.randomUUID(), "profile.png", 3L, "image/png"),
        true
    );

    given(userService.create(eq(request), any())).willReturn(mockUserDto);

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.profile.fileName").value("profile.png"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("사용자 생성 실패 - 중복된 이메일")
  void createUser_duplicateEmail_failure() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("testuser", "duplicate@example.com", "password123");

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest", "", "application/json", objectMapper.writeValueAsBytes(request));

    MockMultipartFile profileFile = new MockMultipartFile(
        "profile", "profile.png", "image/png", new byte[]{1, 2, 3});

    // 서비스가 예외를 던지도록 설정
    given(userService.create(eq(request), any()))
        .willThrow(new DuplicateEmailException(
            Instant.now(),
            ErrorCode.DUPLICATE_EMAIL,
            Map.of("email", "duplicate@example.com")
        ));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isConflict()) // 409
        .andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"))
        .andExpect(jsonPath("$.message").exists());
  }
}