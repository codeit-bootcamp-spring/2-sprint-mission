package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicMessageService messageService;

  @Test
  @DisplayName("delete_성공")
  void delete() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();

    // when
    doNothing().when(messageService).deleteMessage(messageId);

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

  }

  @Test
  @DisplayName("delete_실패_id가 유효하지 않을 때")
  void delete_shouldReturnMessageNotFound_whenMessageIdInvalid() throws Exception {
    // given
    UUID invalidMessageId = UUID.randomUUID();

    // when
    doThrow(new MessageNotFoundException(invalidMessageId))
        .when(messageService).deleteMessage(invalidMessageId);

    // then
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/{messageId}", invalidMessageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("메시지를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));

  }

}
