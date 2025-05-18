package com.sprint.mission.discodeit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void createUser_success() throws Exception {
    String userJson = """
        {
          "username": "testuser",
          "email": "testuser@example.com",
          "password": "password123"
        }
        """;

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("testuser@example.com"));
  }

  @Test
  public void createUser_fail_whenBlankUsername() throws Exception {
    String userJson = """
        {
          "username": "",
          "email": "testuser@example.com",
          "password": "password123"
        }
        """;

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isBadRequest());
  }
}