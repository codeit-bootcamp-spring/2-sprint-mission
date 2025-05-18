package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
@Import({ValidationConfig.class, GlobalExceptionHandler.class})
class BasicUserControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @MockBean
  private UserService userService;

  @MockBean
  private UserStatusService userStatusService;

  @Test
  @DisplayName("POST /api/users - 성공 (프로필 없이) → 201")
  void createUser_success() throws Exception {
    // given
    var req = new UserCreateRequest("alice", "alice@example.com", "pwd1234");
    var dto = new UserDto(
        UUID.randomUUID(),
        "alice",
        "alice@example.com",
        null,
        false
    );
    given(userService.create(any(), any(Optional.class)))
        .willReturn(dto);

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        om.writeValueAsBytes(req)
    );

    // when & then
    mvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"));
  }

  @Test
  @DisplayName("POST /api/users - 검증 실패 (username blank) → 400")
  void createUser_validationError() throws Exception {
    // given
    var bad = new UserCreateRequest("", "bad-email", "");
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        om.writeValueAsBytes(bad)
    );

    // when & then
    mvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.username").exists())
        .andExpect(jsonPath("$.details.email").exists());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} - 성공 → 204")
  void deleteUser_success() throws Exception {
    // nothing to stub: delete returns void
    UUID id = UUID.randomUUID();

    mvc.perform(delete("/api/users/{userId}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} - 실패 (유저 없음) → 404")
  void deleteUser_notFound() throws Exception {
    UUID id = UUID.randomUUID();
    Map<String, Object> details = new HashMap<>();
    details.put("id", id);
    willThrow(new UserNotFoundException(details))
        .given(userService).delete(id);

    mvc.perform(delete("/api/users/{userId}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다"));
  }

  @Test
  @DisplayName("PATCH /api/users/{id}/userStatus - 성공 → 200")
  void updateUserStatus_success() throws Exception {
    UUID id = UUID.randomUUID();
    var req = new UserStatusUpdateRequest(Instant.now());
    var statusDto = new UserStatusDto(id, UUID.randomUUID(), java.time.Instant.now());
    given(userStatusService.updateByUserId(id, req))
        .willReturn(statusDto);

    mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                "/api/users/{userId}/userStatus", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(statusDto.userId().toString()));
  }

  @Test
  @DisplayName("PATCH /api/users/{id}/userStatus - 실패 (유저 없음) → 404")
  void updateUserStatus_notFound() throws Exception {
    UUID id = UUID.randomUUID();
    var req = new UserStatusUpdateRequest(Instant.now());
    Map<String, Object> details = new HashMap<>();
    details.put("id", id);
    willThrow(new UserNotFoundException(details))
        .given(userStatusService).updateByUserId(id, req);

    mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                "/api/users/{userId}/userStatus", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다"));
  }
}
