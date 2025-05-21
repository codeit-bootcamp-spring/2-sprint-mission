package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("메시지 생성 성공 테스트")
  void createMessageSuccess() throws Exception {
    MessageCreateRequest request = new MessageCreateRequest("Hello, World!", UUID.randomUUID(),
        UUID.randomUUID());

    UserDto userDto = new UserDto(UUID.randomUUID(), "testUser", "test@example.com", null, true);

    MessageDto responseDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        request.content(),
        request.channelId(),
        userDto,
        List.of()
    );

    Mockito.when(messageService.create(any(), any())).thenReturn(responseDto);

    MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("Hello, World!"))
        .andExpect(jsonPath("$.author.username").value("testUser"))
        .andExpect(jsonPath("$.channelId").exists())
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("메시지 생성 실패 테스트 - 내용 없음")
  void createMessageFailNoContent() throws Exception {
    MessageCreateRequest request = new MessageCreateRequest("", UUID.randomUUID(),
        UUID.randomUUID());

    MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("입력값 검증에 실패했습니다."))
        .andExpect(jsonPath("$.details.content").value("메시지를 입력하지 않았습니다."));
  }
}
