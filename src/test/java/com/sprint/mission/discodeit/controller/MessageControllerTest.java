package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.PageResponse;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  public void createMessage_Success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    Instant now = Instant.now();
    MessageCreateRequest request = new MessageCreateRequest("message", channelId, userId);
    BinaryContentDto binaryContentDto = new BinaryContentDto(
        UUID.randomUUID(),
        "human.jpg",
        1024L,
        MediaType.IMAGE_JPEG_VALUE
    );
    List<BinaryContentDto> binaryContentList = List.of(binaryContentDto);

    UserDto userDto = new UserDto(userId, "user", "user@gmail.com", null, false);
    MessageDto expected = new MessageDto(messageId, now, now, "message", channelId, userDto,
        binaryContentList);

    given(messageService.create(eq(request), any(List.class))).willReturn(
        expected);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile attachmentPart = new MockMultipartFile(
        "attachments",
        "human.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .file(attachmentPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("message"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.author.id").value(userId.toString()))
        .andExpect(jsonPath("$.attachments[0].fileName").value("human.jpg"));
  }

  @Test
  public void createMessage_Failure_InvalidRequest() throws Exception {
    MessageCreateRequest request = new MessageCreateRequest(
        "",
        null,
        null
    );

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void findAllByChannelId_Success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(
        0, 50, Sort.Direction.DESC, "createdAt");
    Instant cursor = Instant.now();
    UserDto userDto = new UserDto(userId, "user", "user@gmail.com", null, false);

    List<MessageDto> messageList = List.of(
        new MessageDto(
            UUID.randomUUID(),
            cursor.minusSeconds(10),
            cursor.minusSeconds(10),
            "message1",
            channelId,
            userDto,
            new ArrayList<>()
        ),
        new MessageDto(
            UUID.randomUUID(),
            cursor.minusSeconds(20),
            cursor.minusSeconds(20),
            "message2",
            channelId,
            userDto,
            new ArrayList<>()
        )
    );

    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        messageList, cursor.minusSeconds(10),
        pageable.getPageSize(), true, (long) messageList.size());

    given(messageService.findAllByChannelId(eq(channelId), eq(cursor), any(Pageable.class)))
        .willReturn(pageResponse);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].content").value("message1"))
        .andExpect(jsonPath("$.nextCursor").exists())
        .andExpect(jsonPath("$.size").value(50))
        .andExpect(jsonPath("$.hasNext").value(true))
        .andExpect(jsonPath("$.totalElements").value(2));
  }

}