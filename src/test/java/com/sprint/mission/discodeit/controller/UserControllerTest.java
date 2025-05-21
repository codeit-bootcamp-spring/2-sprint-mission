package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
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
  @DisplayName("사용자 생성 성공 테스트")
  void createUserSuccess() throws Exception {
    UserCreateRequest request = new UserCreateRequest("testuser", "test@example.com",
        "securePass123");
    MockMultipartFile requestPart = new MockMultipartFile("userCreateRequest", "",
        "application/json", objectMapper.writeValueAsBytes(request));

    UserDto responseDto = new UserDto(UUID.randomUUID(), "testuser", "test@example.com", null,
        false);

    Mockito.when(userService.create(any(), any())).thenReturn(responseDto);

    mockMvc.perform(
            multipart("/api/users").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.online").value(false));
  }

  @Test
  @DisplayName("사용자 생성 실패 테스트")
  void createUserFail() throws Exception {
    UserCreateRequest invalidRequest = new UserCreateRequest("", "invalid-email", "123");
    MockMultipartFile requestPart = new MockMultipartFile("userCreateRequest", "",
        "application/json", objectMapper.writeValueAsBytes(invalidRequest));

    mockMvc.perform(
            multipart("/api/users").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }
}
