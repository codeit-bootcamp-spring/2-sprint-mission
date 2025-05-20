package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
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
class ChannelApiIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void channel_create_success() throws Exception {
    String request = """
        {
          "name": "공지방",
          "description": "중요한 공지"
        }
        """;

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("공지방"));
  }

  @Test
  void channel_update_fail_nullChannel() throws Exception {
    UUID id = UUID.randomUUID();
    String update = """
        {
          "newName": "변경된 이름",
          "newDescription": "변경된 설명"
        }
        """;

    mockMvc.perform(patch("/api/channels/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(update))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }
}
