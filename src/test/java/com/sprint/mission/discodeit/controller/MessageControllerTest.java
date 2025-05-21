package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.MessageMapperImpl;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapperImpl;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@Import({MessageMapperImpl.class, PageResponseMapperImpl.class})
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MessageMapper messageMapper;

  @Autowired
  private PageResponseMapper pageResponseMapper;

  @Test
  @DisplayName("첨부파일 포함 메시지 생성 성공")
  void createMessage_success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    CreateMessageRequestDTO createMessageRequestDTO = new CreateMessageRequestDTO(
        "testMessage", channelId, authorId);

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createMessageRequestDTO)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments", "attachment.png", MediaType.IMAGE_PNG_VALUE, "attachment".getBytes());

    BinaryContent binaryContent = BinaryContent.builder()
        .filename(attachment.getOriginalFilename())
        .contentType(attachment.getContentType())
        .size(attachment.getSize())
        .build();
    UUID binaryContentId = UUID.randomUUID();
    ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

    FindBinaryContentResult findBinaryContentResult = new FindBinaryContentResult(
        binaryContent.getId(), binaryContent.getFilename(), binaryContent.getSize(),
        binaryContent.getContentType());

    FindUserResult findUserResult = new FindUserResult(authorId, findBinaryContentResult, "user",
        "user@email.com", true);

    CreateMessageResult createMessageResult = new CreateMessageResult(messageId,
        Instant.now(), Instant.now(), List.of(findBinaryContentResult),
        createMessageRequestDTO.content(), channelId, findUserResult);

    given(messageService.create(messageMapper.toCreateMessageCommand(createMessageRequestDTO),
        List.of(attachment))).willReturn(createMessageResult);

    // when + then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("testMessage"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.attachments[0].filename").value("attachment.png"))
        .andExpect(jsonPath("$.author.id").value(findUserResult.id().toString()));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 메시지 생성 실패")
  void createMessage_with_invalidParameter_failed() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    CreateMessageRequestDTO createMessageRequestDTO = new CreateMessageRequestDTO(
        "", channelId, authorId);

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createMessageRequestDTO)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments", "attachment.png", MediaType.IMAGE_PNG_VALUE, "attachment".getBytes());

    CreateMessageResult createMessageResult = new CreateMessageResult(messageId,
        Instant.now(), Instant.now(), List.of(mock(FindBinaryContentResult.class)),
        createMessageRequestDTO.content(), channelId, mock(FindUserResult.class));

    given(messageService.create(messageMapper.toCreateMessageCommand(createMessageRequestDTO),
        List.of(attachment))).willReturn(createMessageResult);

    // when + then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("첨부파일 포함 메시지 수정 성공")
  void updateMessage_success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    UpdateMessageRequestDTO updateMessageRequestDTO = new UpdateMessageRequestDTO(
        "newTestMessage");

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateMessageRequestDTO)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments", "attachment.png", MediaType.IMAGE_PNG_VALUE, "attachment".getBytes());

    BinaryContent binaryContent = BinaryContent.builder()
        .filename(attachment.getOriginalFilename())
        .contentType(attachment.getContentType())
        .size(attachment.getSize())
        .build();
    UUID binaryContentId = UUID.randomUUID();
    ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

    FindBinaryContentResult findBinaryContentResult = new FindBinaryContentResult(
        binaryContent.getId(), binaryContent.getFilename(), binaryContent.getSize(),
        binaryContent.getContentType());

    FindUserResult findUserResult = new FindUserResult(authorId, findBinaryContentResult, "user",
        "user@email.com", true);

    UpdateMessageResult updateMessageResult = new UpdateMessageResult(messageId,
        Instant.now(), Instant.now(), List.of(findBinaryContentResult),
        updateMessageRequestDTO.newContent(), channelId, findUserResult);

    given(messageService.update(messageId,
        messageMapper.toUpdateMessageCommand(updateMessageRequestDTO),
        List.of(attachment))).willReturn(updateMessageResult);

    // when + then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/messages/{messageId}", messageId)
            .file(requestPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("newTestMessage"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 메시지 수정 실패")
  void updateMessage_with_invalidParameter_failed() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    UpdateMessageRequestDTO updateMessageRequestDTO = new UpdateMessageRequestDTO(
        "");

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateMessageRequestDTO)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments", "attachment.png", MediaType.IMAGE_PNG_VALUE, "attachment".getBytes());

    UpdateMessageResult updateMessageResult = new UpdateMessageResult(messageId,
        Instant.now(), Instant.now(), List.of(mock(FindBinaryContentResult.class)),
        updateMessageRequestDTO.newContent(), channelId, mock(FindUserResult.class));

    given(messageService.update(messageId,
        messageMapper.toUpdateMessageCommand(updateMessageRequestDTO),
        List.of(attachment))).willReturn(updateMessageResult);

    // when + then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/messages/{messageId}", messageId)
            .file(requestPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isBadRequest());
  }
}
