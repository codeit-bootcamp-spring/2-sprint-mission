package com.sprint.mission.discodeit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void createMessage_success() throws Exception {
    String json = """
        {
          "content": "Hello, world!",
          "channelId": "00000000-0000-0000-0000-000000000000",
          "authorId": "11111111-1111-1111-1111-111111111111"
        }
        """;

    mockMvc.perform(multipart("/api/messages")
            .file("messageCreateRequest", json.getBytes())
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("Hello, world!"));
  }

  @Test
  public void createMessage_fail_blankContent() throws Exception {
    String json = """
        {
          "content": "",
          "channelId": "00000000-0000-0000-0000-000000000000",
          "authorId": "11111111-1111-1111-1111-111111111111"
        }
        """;

    mockMvc.perform(multipart("/api/messages")
            .file("messageCreateRequest", json.getBytes())
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

}
