package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;

    // 1. 메시지 생성 성공
    @Test
    @DisplayName("메시지 생성 성공")
    void createMessage_success() throws Exception {
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID msgId = UUID.randomUUID();
        MessageCreateRequest req = new MessageCreateRequest("내용", channelId, authorId);

        MessageDto resDto = new MessageDto(
                msgId,
                Instant.now(),
                Instant.now(),
                "내용",
                channelId,
                null,
                List.of()
        );

        // mock
        Mockito.when(messageService.create(any(), any())).thenReturn(resDto);

        MockMultipartFile messageRequest = new MockMultipartFile(
                "messageCreateRequest", "",
                "application/json", objectMapper.writeValueAsBytes(req)
        );

        mockMvc.perform(multipart("/api/messages")
                        .file(messageRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.channelId").value(channelId.toString()));
    }

    // 2. 메시지 생성 실패 - 필수값 누락(channelId)
    @Test
    @DisplayName("메시지 생성 실패 - channelId 누락")
    void createMessage_fail_channelIdBlank() throws Exception {
        MessageCreateRequest req = new MessageCreateRequest("내용", null, UUID.randomUUID());

        MockMultipartFile messageRequest = new MockMultipartFile(
                "messageCreateRequest", "",
                "application/json", objectMapper.writeValueAsBytes(req)
        );

        mockMvc.perform(multipart("/api/messages")
                        .file(messageRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    // 3. 메시지 수정 성공
    @Test
    @DisplayName("메시지 수정 성공")
    void updateMessage_success() throws Exception {
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest req = new MessageUpdateRequest("수정된 내용");

        MessageDto resDto = new MessageDto(
                messageId,
                Instant.now(),
                Instant.now(),
                "수정된 내용",
                UUID.randomUUID(),
                null,
                List.of()
        );

        Mockito.when(messageService.update(eq(messageId), any())).thenReturn(resDto);

        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    // 4. 메시지 수정 실패 - 메시지 없음
    @Test
    @DisplayName("메시지 수정 실패 - 메시지 없음")
    void updateMessage_fail_messageNotFound() throws Exception {
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest req = new MessageUpdateRequest("수정불가");

        Mockito.when(messageService.update(eq(messageId), any()))
                .thenThrow(new MessageNotFoundException(messageId));

        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }
}
