package com.sprint.mission.discodeit.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.controller.UserController;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.Role;
import com.sprint.mission.discodeit.core.user.service.UserService;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void CreateUser_Success() throws Exception {
    // given
    UserCreateRequest createRequest = new UserCreateRequest(
        "testuser",
        "test@example.com",
        "password123"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest", // @RequestPart 이름
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsString(createRequest).getBytes(StandardCharsets.UTF_8)
    );

    MockMultipartFile profilePart = new MockMultipartFile(
        "profile", // @RequestPart 이름
        "profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "profile content".getBytes(StandardCharsets.UTF_8)
    );

    UserDto expectedResponseDto = new UserDto(
        UUID.randomUUID(),
        createRequest.username(),
        createRequest.email(),
        null,
        Role.USER,
        false
    );

    when(userService.create(any(UserCreateRequest.class), any(Optional.class)))
        .thenReturn(expectedResponseDto);

    // when & then
    mockMvc.perform(
            multipart("/api/users")
                .file(requestPart) // JSON 파트 추가
                .file(profilePart) // 파일 파트 추가
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("user").roles("USER")))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(expectedResponseDto.id().toString()))
        .andExpect(jsonPath("$.username").value(expectedResponseDto.username()))
        .andExpect(jsonPath("$.email").value(expectedResponseDto.email()))
        .andExpect(jsonPath("$.role").value(Role.USER.name()))
        .andExpect(jsonPath("$.online").value(false));
  }

  @Test
  void getUserList_Success() throws Exception {
    // given
    UUID u1Id = UUID.randomUUID();
    UUID u2Id = UUID.randomUUID();
    List<UserDto> dtoList = List.of(
        new UserDto(u1Id, "a", "a@a.com", null, Role.USER, false),
        new UserDto(u2Id, "b", "b@b.com", null, Role.USER, false)
    );
    when(userService.findAll()).thenReturn(dtoList);
    // when & then
    mockMvc.perform(get("/api/users")
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("a"))
        .andExpect(jsonPath("$[1].username").value("b"));
  }

  @Test
  void update_Success() throws Exception {
    // given
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "testuser",
        "test@example.com",
        "password123"
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "userUpdateRequest", // @RequestPart 이름
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsString(updateRequest).getBytes(StandardCharsets.UTF_8)
    );

    MockMultipartFile profilePart = new MockMultipartFile(
        "profile", // @RequestPart 이름
        "profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "profile content".getBytes(StandardCharsets.UTF_8)
    );

    UUID userId = UUID.randomUUID();

    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        profilePart.getOriginalFilename(), // "profile.jpg"
        profilePart.getSize(),             // 15L
        profilePart.getContentType()       // "image/jpeg"
    );

    UserDto expectedResponseDto = new UserDto(
        userId,
        updateRequest.newUsername(),
        updateRequest.newEmail(),
        profileDto,
        Role.USER,
        false
    );

    when(userService.update(eq(userId), any(UserUpdateRequest.class), any(Optional.class)))
        .thenReturn(expectedResponseDto);

    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(requestPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            })
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  void delete_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_UserNotFound_Throw404() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    doThrow(new UserException(ErrorCode.USER_NOT_FOUND, userId))
        .when(userService).delete(userId);
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
  }

}
