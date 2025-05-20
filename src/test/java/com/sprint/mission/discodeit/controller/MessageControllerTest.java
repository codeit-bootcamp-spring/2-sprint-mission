package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.TestMultipartUtil;
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
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID channelId;
    private UUID authorId;
    private UUID messageId;
    private UserDto author;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        author = new UserDto(authorId, "user", "user@test.com", null, true);
    }

    @Test
    void 메세지_생성_테스트() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest(channelId, authorId, "test");
        MockMultipartFile messageJson = TestMultipartUtil.jsonPart("messageCreateRequest", request);
        MockMultipartFile file1 = new MockMultipartFile("file", "test".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test".getBytes());

        BinaryContentDto binaryContent1Dto = new BinaryContentDto(UUID.randomUUID(),
            file1.getOriginalFilename(), file1.getSize(),
            file1.getContentType(), file1.getBytes());

        BinaryContentDto binaryContent2Dto = new BinaryContentDto(UUID.randomUUID(),
            file2.getOriginalFilename(), file2.getSize(),
            file2.getContentType(), file2.getBytes());

        List<BinaryContentDto> attachments = List.of(binaryContent1Dto, binaryContent2Dto);

        MessageDto messageDto = new MessageDto(messageId, Instant.now(), null, request.content(),
            request.channelId(), author, attachments);

        given(messageService.sendMessage(any(), any())).willReturn(messageDto);

        mockMvc.perform(multipart("/api/messages")
                .file(messageJson)
                .file(file1)
                .file(file2)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void 메세지_생성_내용_없음_테스트() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest(channelId, authorId, null);
        MockMultipartFile messageJson = TestMultipartUtil.jsonPart("messageCreateRequest", request);

        mockMvc.perform(multipart("/api/messages")
                .file(messageJson)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 메세지_수정_테스트() throws Exception {
        MessageUpdateRequest request = new MessageUpdateRequest("test");

        MessageDto messageDto = new MessageDto(messageId, Instant.now(), Instant.now(),
            request.newContent(), channelId, author, null);

        given(messageService.updateMessage(any(), any())).willReturn(messageDto);

        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 메세지_수정_빈_내용_테스트() throws Exception {
        MessageUpdateRequest request = new MessageUpdateRequest("");

        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 메세지_삭제_테스트() throws Exception {
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void 채널의_메시지_목록_조회_테스트() throws Exception {
        UUID channelId = UUID.randomUUID();
        Instant cursor = Instant.now();

        MessageDto message1Dto = new MessageDto(
            UUID.randomUUID(), Instant.now().minusSeconds(30), null, "첫 번째 메시지", channelId, author,
            null
        );
        MessageDto message2Dto = new MessageDto(
            UUID.randomUUID(), Instant.now(), null, "두 번째 메시지", channelId, author, null
        );
        List<MessageDto> messageDtos = List.of(message1Dto, message2Dto);

        PageResponse<MessageDto> response = new PageResponse<>(
            messageDtos,
            Instant.now(),
            0,
            false,
            2L
        );

        given(messageService.findMessageByChannelId(any(), any(), any()))
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .param("cursor", cursor.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].content").value("첫 번째 메시지"))
            .andExpect(jsonPath("$.content[1].content").value("두 번째 메시지"))
            .andDo(print());
    }
}