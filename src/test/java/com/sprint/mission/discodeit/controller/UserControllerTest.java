package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.JwtAuthInterceptor;
import com.sprint.mission.discodeit.auth.JwtUtil;
import com.sprint.mission.discodeit.core.user.controller.UserController;
import com.sprint.mission.discodeit.core.user.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
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

  @MockitoBean
  private UserService userService;

  @MockitoBean
  JwtUtil jwtUtil;

  @MockitoBean
  JwtAuthInterceptor intercept;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void CreateUser_Success() throws Exception {
    // given
    String givenName = "test";
    String givenEmail = "test@test.com";
    String givenPassword = "test";
    UUID userId = UUID.randomUUID();

    UserCreateRequest request = new UserCreateRequest(
        givenName, givenEmail, givenPassword);

    UserDto expectResult = new UserDto(
        userId, givenName, givenEmail, null, false);

    when(userService.create(any(UserCreateCommand.class), eq(Optional.empty()))).thenReturn(
        expectResult);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value(givenName))
        .andExpect(jsonPath("$.email").value(givenEmail));
  }

  @Test
  void CreateUser_AlreadyUserName_Exists() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest(
        "test", "test@test.com", "test");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    doThrow(new UserAlreadyExistsException(ErrorCode.USER_NAME_ALREADY_EXISTS, "test"))
        .when(userService).create(any(UserCreateCommand.class), eq(Optional.empty()));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NAME_ALREADY_EXISTS.getCode()));
  }

  @Test
  void CreateUser_AlreadyUserEmail_Exists() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest(
        "test", "test@test.com", "test");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    doThrow(new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, "test@test.com"))
        .when(userService).create(any(UserCreateCommand.class), eq(Optional.empty()));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getCode()));
  }

  @Test
  void getUserList_Success() throws Exception {
    // given
    UUID u1Id = UUID.randomUUID();
    UUID u2Id = UUID.randomUUID();
    List<UserDto> dtoList = List.of(
        new UserDto(u1Id, "a", "a@a.com", null, false),
        new UserDto(u2Id, "b", "b@b.com", null, false)
    );
    when(userService.findAll()).thenReturn(dtoList);
    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("a"))
        .andExpect(jsonPath("$[1].username").value("b"));
  }

  @Test
  void update_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserDto expectUser = new UserDto(userId, "b", "b@b.com", null, true);
    UserUpdateRequest updateRequest = new UserUpdateRequest("b", "b@b.com", "b");

    when(userService.update(any(UserUpdateCommand.class), eq(Optional.empty()))).thenReturn(
        expectUser);
    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(updateRequest)
    );
    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("b"))
        .andExpect(jsonPath("$.email").value("b@b.com"));
  }

  @Test
  void update_UserNotFound_Throw404() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest("b", "b@b.com", "b");

    doThrow(new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId))
        .when(userService).update(any(UserUpdateCommand.class), eq(Optional.empty()));

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(updateRequest)
    );
    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
  }

  @Test
  void delete_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void delete_UserNotFound_Throw404() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    doThrow(new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId))
        .when(userService).delete(userId);
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
  }

}
