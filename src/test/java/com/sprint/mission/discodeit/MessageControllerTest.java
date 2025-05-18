package com.sprint.mission.discodeit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MessageService messageService;

  @Test
  @DisplayName("메시지 생성 - 성공")
  void createMessage_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest req = new MessageCreateRequest("hello", channelId, authorId);

    MessageDto res = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        req.content(),
        channelId,
        new UserDto(
            authorId,
            "작성자",
            "author@example.com",
            null,
            true
        ),
        List.of()
    );

    when(messageService.create(any(), any())).thenReturn(res);

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(req)
    );

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("hello"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()));
  }

  @Test
  @DisplayName("메시지 삭제 - 성공")
  void deleteMessage_success() throws Exception {
    UUID messageId = UUID.randomUUID();

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("채널 메시지 조회 - 성공")
  void findMessagesByChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();

    when(messageService.findAllByChannelId(any(), any(), any()))
        .thenReturn(new PageResponse<>(
            List.of(),
            null,
            0,
            false,
            0L
        ));

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isOk());
  }
}