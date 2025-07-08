package com.sprint.mission.discodeit.message.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.channel.ChannelException;
import com.sprint.mission.discodeit.core.message.MessageException;
import com.sprint.mission.discodeit.core.message.controller.MessageController;
import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.service.MessageService;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;

  UUID userId;
  UUID channelId;
  UUID messageId;
  Instant now;

  @BeforeEach
  void setUp() {
    now = Instant.now();
    userId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    messageId = UUID.randomUUID();
  }

  @Test
  void create_success() throws Exception {
    // given
    MessageCreateRequest request = new MessageCreateRequest(userId, channelId, "test");
    MessageDto messageDto = new MessageDto(messageId, now, now, "test",
        channelId,
        null, List.of());

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    when(messageService.create(any(), anyList())).thenReturn(
        messageDto);
    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.content").value("test"));
  }

  @Test
  void create_UserNotFound_Throw404() throws Exception {
    // given
    MessageCreateRequest request = new MessageCreateRequest(userId, channelId, "test");
    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );
    doThrow(new UserException(ErrorCode.USER_NOT_FOUND, userId))
        .when(messageService).create(any(), anyList());
    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
  }

  @Test
  void create_ChannelNotFound_Throw404() throws Exception {
    // given
    MessageCreateRequest request = new MessageCreateRequest(userId, channelId, "test");
    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );
    doThrow(new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, channelId))
        .when(messageService).create(any(), anyList());
    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()));
  }

  @Test
  void update_Success() throws Exception {
    // given
    MessageDto messageDto = new MessageDto(messageId, now, now, "aaa",
        channelId,
        null, List.of());
    when(messageService.update(eq(messageId), any(MessageUpdateRequest.class))).thenReturn(
        messageDto);
    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .with(csrf())
            .with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(mock(MessageUpdateRequest.class))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("aaa"));
  }


  @Test
  void delete_Success() throws Exception {
    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_MessageNotFound_Throw404() throws Exception {
    // given
    doThrow(new MessageException(ErrorCode.MESSAGE_NOT_FOUND, messageId)).when(
        messageService).delete(messageId);
    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.getCode()));
  }
}
