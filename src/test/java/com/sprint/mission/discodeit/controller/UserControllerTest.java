package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.controller.ValidationConfig;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({ValidationConfig.class})  // @Validated, MethodValidationPostProcessor 등 설정
class UserControllerTest {

  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper om;

  @MockBean
  UserService userService;
  @MockBean
  UserStatusService userStatusService;

  @Test
  @DisplayName("POST /api/users - 성공 (프로필 없이) → 201")
  void createUser_success() throws Exception {
    // given
    var req = new UserCreateRequest("alice", "alice@example.com", "pwd1234");
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        om.writeValueAsBytes(req)
    );

    var returned = new UserDto(
        UUID.randomUUID(),
        "alice",
        "alice@example.com",
        null,  // profile 없음
        false  // online
    );
    given(userService.create(req, Optional.empty())).willReturn(returned);

    // when & then
    mvc.perform(multipart("/api/users")
            .file(userPart)
            .characterEncoding("UTF-8")  // validation 오류 방지
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(returned.id().toString()))
        .andExpect(jsonPath("$.username").value("alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"))
        .andExpect(jsonPath("$.profile").isEmpty())
        .andExpect(jsonPath("$.online").value(false));
  }

  @Test
  @DisplayName("POST /api/users - 검증 실패 (username blank) → 400")
  void createUser_validationError() throws Exception {
    // given: username 빈 문자열
    var req = new UserCreateRequest("", "bad-email", "");
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        om.writeValueAsBytes(req)
    );

    // when & then
    mvc.perform(multipart("/api/users")
            .file(userPart)
            .characterEncoding("UTF-8")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.username").exists())
        .andExpect(jsonPath("$.details.email").exists());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} - 성공 → 204")
  void deleteUser_success() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    // userService.delete(id) 에서 예외 안 던지면 정상
    
    mvc.perform(delete("/api/users/{id}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} - 실패 (유저 없음) → 404")
  void deleteUser_notFound() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    willThrow(new UserNotFoundException())
        .given(userService).delete(id);

    // when & then
    mvc.perform(delete("/api/users/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("유저를 찾을 수 없습니다"));  // GlobalExceptionHandler에서 설정한 메시지
  }

  @Test
  @DisplayName("GET /api/users - 성공 → 200")
  void findAllUsers_success() throws Exception {
    // given
    var u1 = new UserDto(UUID.randomUUID(), "u1", "u1@e.com", null, true);
    var u2 = new UserDto(UUID.randomUUID(), "u2", "u2@e.com", null, false);
    given(userService.findAll()).willReturn(List.of(u1, u2));

    // when & then
    mvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("u1"))
        .andExpect(jsonPath("$[1].username").value("u2"));
  }

  @Test
  @DisplayName("PATCH /api/users/{id}/userStatus - 성공 → 200")
  void updateUserStatus_success() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    var req = new UserStatusUpdateRequest(Instant.now());
    var dto = new UserStatusDto(UUID.randomUUID(), id, Instant.now());
    given(userStatusService.updateByUserId(id, req)).willReturn(dto);

    // when & then
    mvc.perform(patch("/api/users/{id}/userStatus", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(id.toString()))
        .andExpect(jsonPath("$.id").value(dto.id().toString()));
  }
}
