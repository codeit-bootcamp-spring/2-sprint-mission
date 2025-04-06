package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.application.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.JsonConvertor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private MessageService messageService;

    @Test
    void create() {
        Message message = new Message(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID(), List.of());
        MessageResult stubResult = MessageResult.fromEntity(message);
        when(messageService.create(any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/messages")
                .multipart()
                .file(new MockMultipartFile("messageCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                        JsonConvertor.asString(new MessageCreateRequest(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID())).getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(message.getId().toString());
    }

    @Test
    void getByChannelId() {
        UUID channelId = UUID.randomUUID();
        Message message = new Message(MESSAGE_CONTENT, channelId, UUID.randomUUID(), List.of());
        MessageResult stubResult = MessageResult.fromEntity(message);

        when(messageService.getAllByChannelId(any())).thenReturn(List.of(stubResult));

        assertThat(mockMvc.get()
                .uri("/api/messages")
                .queryParam("channelId", channelId.toString()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].id")
                .isEqualTo(List.of(message.getId().toString()));
    }

    @Test
    void updatePublic() {
        Message message = new Message(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID(), List.of());
        message.updateContext(MESSAGE_CONTENT + "123");
        MessageResult stubResult = MessageResult.fromEntity(message);

        when(messageService.updateContext(any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.patch()
                .uri("/api/messages/{messageId}", message.getId())
                .content(JsonConvertor.asString(new MessageUpdateRequest(MESSAGE_CONTENT + "123")))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.content")
                .isEqualTo(MESSAGE_CONTENT + "123");
    }

    @Test
    void delete() {
        UUID messageId = UUID.randomUUID();
        assertThat(mockMvc.delete().uri("/api/messages/{id}", messageId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}