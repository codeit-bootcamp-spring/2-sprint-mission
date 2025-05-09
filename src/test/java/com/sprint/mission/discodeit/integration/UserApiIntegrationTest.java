package com.sprint.mission.discodeit.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  private UUID userId;

  @BeforeEach
  void setUp() {
    User user = new User("john", "john@email.com", "1234", null);
    userRepository.save(user);
    userId = user.getId();
  }

  @Test
  void updateUser_success() throws Exception {
    String updateBody = """
        {
          "newUsername": "johnny",
          "newEmail": "johnny@email.com",
          "newPassword": "5678"
        }
        """;

    mockMvc.perform(patch("/api/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("johnny"));
  }

  @Test
  void updateUser_fail_invalidEmailFormat() throws Exception {
    String invalidEmail = """
        {
          "newUsername": "baduser",
          "newEmail": "not-an-email",
          "newPassword": "pw"
        }
        """;

    mockMvc.perform(patch("/api/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidEmail))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("METHOD_ARGUMENT_NOT_VALID"));
  }

  @Test
  void deleteUser_success() throws Exception {
    mockMvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteUser_fail_userNotFound() throws Exception {
    UUID unknownId = UUID.randomUUID();

    mockMvc.perform(delete("/api/users/" + unknownId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
  }

  @Test
  void getUser_success() throws Exception {
    mockMvc.perform(get("/api/users/" + userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("john"));
  }

  @Test
  void getUser_fail_userNotFound() throws Exception {
    UUID unknownId = UUID.randomUUID();

    mockMvc.perform(get("/api/users/" + unknownId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
  }

  @Test
  void user_create_success() throws Exception {
    String body = """
        {
          "username": "john",
          "email": "john@email.com",
          "password": "1234"
        }
        """;

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("john"));
  }

  @Test
  void user_create_fail_duplicatedEmail() throws Exception {
    // 먼저 생성
    String body = """
        {
          "username": "john",
          "email": "john@email.com",
          "password": "1234"
        }
        """;
    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body));

    // 다시 생성 시도
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("USER_ALREADY_EXISTS"));
  }
}

