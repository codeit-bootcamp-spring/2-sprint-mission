package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicUserService userService;

  @MockitoBean
  private BasicUserStatusService userStatusService;

  @Test
  @DisplayName("Create User_성공")
  void create_shouldReturnCreatedUser() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    UserCreateRequest userCreateRequest = new UserCreateRequest("test@email.com", "password123!",
        "testUser");

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(userCreateRequest)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        MediaType.IMAGE_PNG_VALUE,
        "fake-image-content".getBytes()
    );

    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "profile.png",
        3L,
        "image/png"
    );

    UserDto userDto = new UserDto(userId, "testUser", "test@email.com", profileDto, true);

    // when
    when(userService.createUser(any(), any())).thenReturn(userDto);

    // then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.email").value("test@email.com"))
        .andExpect(jsonPath("$.profile.fileName").value("profile.png"))
        .andExpect(jsonPath("$.profile.size").value(3L))
        .andExpect(jsonPath("$.profile.contentType").value("image/png"));
  }

  @Test
  @DisplayName("Create User_실패_이메일 형식 불일치")
  void create_shouldReturnBadRequest_whenEmailInvalid() throws Exception {
    // given
    UserCreateRequest invalidRequest = new UserCreateRequest("invalid-email", "password123!",
        "testUser");

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(invalidRequest)
    );

    // when, then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.details.email").value("올바른 이메일 형식이 아닙니다."));
  }

}
