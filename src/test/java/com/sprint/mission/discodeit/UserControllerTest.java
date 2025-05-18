package com.sprint.mission.discodeit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @MockBean
  private UserStatusService userStatusService;

  @Test
  @DisplayName("사용자 생성 - 성공")
  void createUser_success() throws Exception {
    UserCreateRequest req = new UserCreateRequest(
        "testuser",
        "test@example.com",
        "pass123"
    );

    UserDto responseDto = new UserDto(
        UUID.randomUUID(),
        "testuser",
        "test@example.com",
        null,
        true
    );

    when(userService.create(any(), any())).thenReturn(responseDto);

    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(req)
    );

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  @DisplayName("사용자 생성 - 실패 (잘못된 요청)")
  void createUser_failure_badRequest() throws Exception {
    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        "{}".getBytes()
    );

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("사용자 상태 업데이트 - 성공")
  void updateUserStatus_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest req = new UserStatusUpdateRequest(Instant.now());

    UserStatusDto resDto = new UserStatusDto(
        UUID.randomUUID(),
        userId,
        req.newLastActiveAt()
    );

    when(userStatusService.updateByUserId(Mockito.eq(userId), any())).thenReturn(resDto);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.newLastActiveAt").exists());
  }

  @Test
  @DisplayName("사용자 상태 업데이트 - 실패 (잘못된 상태 값)")
  void updateUserStatus_failure_badRequest() throws Exception {
    UUID userId = UUID.randomUUID();

    String invalidRequest = "{}";

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequest))
        .andExpect(status().isBadRequest());
  }
}
