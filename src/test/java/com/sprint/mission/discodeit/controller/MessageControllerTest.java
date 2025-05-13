package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.exception.message.MessageNotFound;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    // 1. 메시지 생성 성공 (첨부파일 없음)
    @Test
    @DisplayName("메시지 생성 성공 - 201 Created")
    void createMessage_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        MessageCreateRequest request = new MessageCreateRequest("채널ID", channelId, authorId);
        MessageDto response = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "메시지내용", channelId, new UserDto(authorId, "username", "userEmail", null, true), Collections.emptyList());


        given(messageService.create(any(), any())).willReturn(response);

        MockMultipartFile messageRequest = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/messages")
                        .file(messageRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("메시지내용"));
    }

    // 2. 메시지 생성 실패 - 유효성 검증 실패
    @Test
    @DisplayName("메시지 생성 실패 - 유효성 검증 (400 Bad Request)")
    void createMessage_fail_validation() throws Exception {
        // given: content가 null인 잘못된 요청
        MessageCreateRequest request = new MessageCreateRequest("채널ID", UUID.randomUUID(), null); // content = null

        MockMultipartFile messageRequest = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/messages")
                        .file(messageRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()); // 400 검증
    }

    // 3. 메시지 수정 성공
    @Test
    @DisplayName("메시지 수정 성공 - 200 OK")
    void updateMessage_success() throws Exception {
        // given
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지");
        MessageDto response = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "메시지내용", channelId, new UserDto(authorId, "username", "userEmail", null, true), Collections.emptyList());

        given(messageService.update(eq(messageId), any())).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("메시지내용"));
    }

    // 4. 메시지 수정 실패 - 메시지 없음
    @Test
    @DisplayName("메시지 수정 실패 - 메시지 없음 (404 Not Found)")
    void updateMessage_fail_notFound() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("수정 시도");

        given(messageService.update(eq(messageId), any()))
                .willThrow(new MessageNotFound());

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // 5. 메시지 삭제 성공
    @Test
    @DisplayName("메시지 삭제 성공 - 204 No Content")
    void deleteMessage_success() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());
    }

    // 6. 메시지 삭제 실패 - 메시지 없음
    @Test
    @DisplayName("메시지 삭제 실패 - 메시지 없음 (404 Not Found)")
    void deleteMessage_fail_notFound() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        willThrow(new MessageNotFound()).given(messageService).delete(messageId);

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNotFound());
    }
}
