package com.sprint.mission.discodeit.integration;

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
class MessageApiIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void message_create_fail_nullChannel() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    String body = String.format("""
        {
          "channelId": "%s",
          "authorId": "%s",
          "content": "안녕하세요"
        }
        """, channelId, authorId);

    mockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }
}
