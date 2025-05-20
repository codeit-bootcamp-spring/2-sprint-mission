package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    private MessageCreateRequest messageCreateRequest;
    private MessageUpdateRequest messageUpdateRequest;
    private MessageDto messageDto;
    private PageResponse<MessageDto> pageResponseMessageDto;
    private UUID messageId;
    private UUID channelId;
    private UUID authorId;
    private Instant now;
    private UserDto authorDto;

    @BeforeEach
    void setUp() {
        messageId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        now = Instant.now();

        authorDto = new UserDto(authorId, "testAuthor", "author@mail.com", null, true);
        messageCreateRequest = new MessageCreateRequest("Test Content", channelId, authorId);
        messageUpdateRequest = new MessageUpdateRequest("Updated Content");
        messageDto = new MessageDto(messageId, now, now, "Test Content", channelId, authorDto,
            Collections.emptyList());
        pageResponseMessageDto = new PageResponse<>(Collections.singletonList(messageDto), null, 1,
            false, 1L);
    }

    @Test
    @DisplayName("메시지 생성 - 성공 (첨부파일 없음)")
    void createMessage_Success_NoAttachments() throws Exception {
        // given
        when(messageService.create(any(MessageCreateRequest.class), anyList()))
            .thenReturn(messageDto);

        String requestJson = objectMapper.writeValueAsString(messageCreateRequest);
        MockMultipartFile requestPart = new MockMultipartFile(
            "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            requestJson.getBytes());

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(requestPart)
                .characterEncoding("UTF-8")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("메시지 생성 - 성공 (첨부파일 포함)")
    void createMessage_Success_WithAttachments() throws Exception {
        // given
        when(messageService.create(any(MessageCreateRequest.class), anyList()))
            .thenReturn(messageDto);

        String requestJson = objectMapper.writeValueAsString(messageCreateRequest);
        MockMultipartFile requestPart = new MockMultipartFile(
            "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            requestJson.getBytes());

        MockMultipartFile file1 = new MockMultipartFile("attachments", "file1.txt",
            MediaType.TEXT_PLAIN_VALUE, "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("attachments", "file2.txt",
            MediaType.TEXT_PLAIN_VALUE, "content2".getBytes());

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(requestPart)
                .file(file1)
                .file(file2)
                .characterEncoding("UTF-8")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("메시지 업데이트 - 성공")
    void updateMessage_Success() throws Exception {
        // given
        MessageDto updatedDto = new MessageDto(messageId, now, Instant.now(),
            messageUpdateRequest.newContent(), channelId, authorDto, Collections.emptyList());
        when(messageService.update(eq(messageId), any(MessageUpdateRequest.class)))
            .thenReturn(updatedDto);

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageUpdateRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("메시지 삭제 - 성공")
    void deleteMessage_Success() throws Exception {
        // given
        doNothing().when(messageService).delete(messageId);

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("메시지 삭제 - 실패 (메시지 없음)")
    void deleteMessage_Failure_NotFound() throws Exception {
        // given
        doThrow(
            new MessageException(com.sprint.mission.discodeit.exception.ErrorCode.MESSAGE_NOT_FOUND,
                Collections.emptyMap()))
            .when(messageService).delete(messageId);

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNotFound()); // Assuming GlobalExceptionHandler maps this to 404
    }

    @Test
    @DisplayName("채널별 메시지 목록 조회 - 성공")
    void getMessagesByChannelId_Success() throws Exception {
        // given
        when(messageService.findAllByChannelId(eq(channelId), any(), anyInt()))
            .thenReturn(pageResponseMessageDto);

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .param("size", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("채널별 메시지 목록 조회 - 커서 사용 성공")
    void getMessagesByChannelId_WithCursor_Success() throws Exception {
        // given
        String cursor = Instant.now().toString();
        PageResponse<MessageDto> pagedResponseWithCursor = new PageResponse<>(
            Collections.singletonList(messageDto), "nextCursor", 1, true, 10L);
        when(messageService.findAllByChannelId(eq(channelId), eq(cursor), anyInt()))
            .thenReturn(pagedResponseWithCursor);

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .param("cursor", cursor)
                .param("size", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
} 