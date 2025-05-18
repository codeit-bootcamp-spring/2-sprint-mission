package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.InvalidMessageAuthorException;
import com.sprint.mission.discodeit.handler.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    @DisplayName("POST /api/messages - 메시지 생성 성공")
    void createMessage_success() throws Exception {
        // given
        CreateMessageRequest request = new CreateMessageRequest(UUID.randomUUID(),
            UUID.randomUUID(),
            "메시지입니다", List.of(UUID.randomUUID()));

        MessageDto response = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(),
            request.content(), request.channelId(),
            new UserDto(UUID.randomUUID(), "testuser", "test@example.com", null, true),
            List.of()
        );

        given(messageService.createMessage(any(CreateMessageRequest.class), any()))
            .willReturn(response);

        MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request)
        );

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("메시지입니다"))
            .andExpect(jsonPath("$.channelId").value(request.channelId().toString()))
            .andExpect(jsonPath("$.author.username").value("testuser"))
            .andExpect(jsonPath("$.attachments").isArray());
    }

    @Test
    @DisplayName("POST /api/messages - 작성자 누락으로 인한 실패")
    void createMessage_invalidAuthor_failure() throws Exception {
        // given
        CreateMessageRequest request = new CreateMessageRequest(null, UUID.randomUUID(),
            "내용 있음", List.of());

        given(messageService.createMessage(any(CreateMessageRequest.class), any()))
            .willThrow(new InvalidMessageAuthorException());

        MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request));

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
            .andExpect(jsonPath("$.message").value("요청값이 유효하지 않습니다."))
            .andExpect(jsonPath("$.details.errors[0].field").value("authorId"))
            .andExpect(jsonPath("$.details.errors[0].message").value("작성자 ID는 필수입니다."));
    }

    @Test
    @DisplayName("PATCH /api/messages/{id} - 메시지 수정 성공")
    void updateMessage_success() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        UpdateMessageRequest request = new UpdateMessageRequest("수정된 내용");

        MessageDto response = new MessageDto(messageId, Instant.now(), Instant.now(),
            "수정된 내용", UUID.randomUUID(),
            new UserDto(UUID.randomUUID(), "testuser", "test@example.com", null, true),
            List.of()
        );

        given(messageService.updateMessage(any(UUID.class), any(UpdateMessageRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/messages/{id}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(messageId.toString()))
            .andExpect(jsonPath("$.content").value("수정된 내용"))
            .andExpect(jsonPath("$.author.username").value("testuser"));
    }

    @Test
    @DisplayName("DELETE /api/messages/{id} - 메시지 삭제 성공")
    void deleteMessage_success() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/messages/{id}", messageId))
            .andExpect(status().isNoContent());
    }
}
