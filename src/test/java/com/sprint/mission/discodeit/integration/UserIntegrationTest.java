package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private UserService service;

  @Test
  @DisplayName("사용자 생성 성공")
  void createUserSuccessfully() throws Exception {
    UserCreateRequest request = new UserCreateRequest("newuser", "newuser@example.com",
        "StrongPass1!");

    MockMultipartFile userPart = new MockMultipartFile("userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        mapper.writeValueAsBytes(request));
    MockMultipartFile profilePart = new MockMultipartFile("profile", "avatar.png",
        MediaType.IMAGE_PNG_VALUE,
        "image-bytes".getBytes());

    mvc.perform(MockMvcRequestBuilders.multipart("/api/users")
            .file(userPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", not(emptyString())))
        .andExpect(jsonPath("$.username", is("newuser")))
        .andExpect(jsonPath("$.email", is("newuser@example.com")))
        .andExpect(jsonPath("$.profile.fileName", is("avatar.png")))
        .andExpect(jsonPath("$.online", is(true)));
  }

  @Test
  @DisplayName("사용자 생성 실패 - 입력값 오류")
  void createUserFailure_invalidInput() throws Exception {
    UserCreateRequest invalidRequest = new UserCreateRequest("a", "invalidemail", "123");

    MockMultipartFile invalidUserPart = new MockMultipartFile("userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        mapper.writeValueAsBytes(invalidRequest));

    mvc.perform(MockMvcRequestBuilders.multipart("/api/users")
            .file(invalidUserPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("사용자 정보 수정 성공")
  void updateUserSuccessfully() throws Exception {
    UserCreateRequest createReq = new UserCreateRequest("origuser", "orig@example.com",
        "Password123!");
    UserDto created = service.create(createReq, Optional.empty());

    UserUpdateRequest updateReq = new UserUpdateRequest("updateduser", "updated@example.com",
        "NewPass123!");

    MockMultipartFile updatePart = new MockMultipartFile("userUpdateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        mapper.writeValueAsBytes(updateReq));

    MockMultipartFile newProfile = new MockMultipartFile("profile", "new-profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "new-image-bytes".getBytes());

    mvc.perform(MockMvcRequestBuilders.multipart("/api/users/{id}", created.id())
            .file(updatePart)
            .file(newProfile)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(created.id().toString())))
        .andExpect(jsonPath("$.username", is("updateduser")))
        .andExpect(jsonPath("$.email", is("updated@example.com")))
        .andExpect(jsonPath("$.profile.fileName", is("new-profile.jpg")));
  }

  @Test
  @DisplayName("사용자 수정 실패 - 사용자 없음")
  void updateUserFailure_userNotFound() throws Exception {
    UUID fakeId = UUID.randomUUID();
    UserUpdateRequest updateReq = new UserUpdateRequest("nouser", "nouser@example.com",
        "Pass1234!");

    MockMultipartFile updatePart = new MockMultipartFile("userUpdateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        mapper.writeValueAsBytes(updateReq));

    mvc.perform(MockMvcRequestBuilders.multipart("/api/users/{id}", fakeId)
            .file(updatePart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            }))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void deleteUserSuccessfully() throws Exception {
    UserCreateRequest request = new UserCreateRequest("tobedeleted", "del@example.com",
        "Password123!");
    UserDto user = service.create(request, Optional.empty());

    mvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user.id()))
        .andExpect(status().isNoContent());

    mvc.perform(MockMvcRequestBuilders.get("/api/users")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id=='" + user.id().toString() + "')]").doesNotExist());
  }

  @Test
  @DisplayName("사용자 삭제 실패 - 사용자 없음")
  void deleteUserFailure_userNotFound() throws Exception {
    UUID missingId = UUID.randomUUID();

    mvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", missingId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자 전체 조회 성공")
  void fetchAllUsersSuccessfully() throws Exception {
    UserCreateRequest first = new UserCreateRequest("alpha", "alpha@example.com", "Password123!");
    UserCreateRequest second = new UserCreateRequest("beta", "beta@example.com", "Password123!");

    service.create(first, Optional.empty());
    service.create(second, Optional.empty());

    mvc.perform(MockMvcRequestBuilders.get("/api/users")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$[?(@.username == 'alpha')]").exists())
        .andExpect(jsonPath("$[?(@.username == 'beta')]").exists());
  }
}
