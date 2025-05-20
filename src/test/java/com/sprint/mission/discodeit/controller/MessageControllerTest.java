package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  private static final String BASE_URL = "/api/messages";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private BinaryContentService binaryContentService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserRepository userRepository;

  @DisplayName("GET /api/messages - 채널별 메시지 페이징 조회 성공")
  @Test
  void findAllByChannelId_Success() throws Exception {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);
    Instant cursor = Instant.now();

    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "test",
        UUID.randomUUID(),
        null,
        List.of()
    );

    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        List.of(messageDto), false, 1, false, 1L);

    given(messageService.findMessagesByCursor(eq(channelId), eq(cursor), any(Pageable.class)))
        .willReturn(pageResponse);

    mockMvc.perform(get(BASE_URL)
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString())
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));
  }

  @DisplayName("POST /api/messages - 메시지 생성 (첨부파일 없음) 성공")
  @Test
  void createMessage_WithoutAttachments_Success() throws Exception {
    CreateMessageRequest request = new CreateMessageRequest(
        "test",
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    MessageDto responseDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "test",
        UUID.randomUUID(),
        null,
        List.of()
    );

    given(messageService.createMessage(any(CreateMessageRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(multipart(BASE_URL)
            .file(new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)))
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDto.id().toString()));
  }

  @DisplayName("POST /api/messages - 메시지 생성 (첨부파일 포함) 성공")
  @Test
  void createMessage_WithAttachments_Success() throws Exception {
    CreateMessageRequest request = new CreateMessageRequest(
        "test",
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    MessageDto responseDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "test",
        UUID.randomUUID(),
        null,
        List.of()
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments",
        "file.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "file content".getBytes(StandardCharsets.UTF_8)
    );

    given(messageService.createMessage(any(CreateMessageRequest.class)))
        .willReturn(responseDto);

    given(binaryContentService.createBinaryContent(any()))
        .willReturn(mock(BinaryContent.class));

    mockMvc.perform(multipart(BASE_URL)
            .file(new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)))
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDto.id().toString()));
  }

  @DisplayName("PATCH /api/messages/{messageId} - 메시지 수정 성공")
  @Test
  void updateMessage_Success() throws Exception {
    UUID messageId = UUID.randomUUID();

    UpdateMessageRequest request = new UpdateMessageRequest(
        "test"
    );

    MessageDto responseDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "test",
        UUID.randomUUID(),
        null,
        List.of()
    );

    given(messageService.updateMessage(eq(messageId), any(UpdateMessageRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(patch(BASE_URL + "/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()));
  }

  @DisplayName("DELETE /api/messages/{messageId} - 메시지 삭제 성공")
  @Test
  void deleteMessage_Success() throws Exception {
    UUID messageId = UUID.randomUUID();

    mockMvc.perform(delete(BASE_URL + "/" + messageId))
        .andExpect(status().isNoContent());
  }
}
