package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MessageService messageService;

  @MockBean
  private UserService userService;

  @Test
  void message_find_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    PageResponse<MessageDto> response = new PageResponse<MessageDto>(
        List.of(new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "sss",
            channelId, null, null)),
        null,
        10,
        false,
        Long.valueOf(20)
    );

    given(messageService.findAllByChannelId(eq(channelId), any(), any())).willReturn(response);

    mockMvc.perform(get("/api/messages/channel/" + channelId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].content").value("내용"));
  }

  @Test
  void message_find_fail() throws Exception {
    UUID channelId = UUID.randomUUID();
    given(messageService.findAllByChannelId(eq(channelId), any(), any()))
        .willThrow(new ChannelNotFoundException(channelId));

    mockMvc.perform(get("/api/messages/channel/" + channelId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }
}

