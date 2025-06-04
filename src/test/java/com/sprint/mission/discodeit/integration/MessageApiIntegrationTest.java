package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.time.OffsetDateTime;
import java.time.Instant;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BinaryContentService binaryContentService;

    @MockitoBean
    private BinaryContentRepository binaryContentRepository;

    private String testUserId;
    private String testChannelId;

    private static final String MESSAGE = "test message";
    private static final String USERNAME = "user";
    private static final String EMAIL = "user@naver.com";
    private static final String PASSWORD = "123456";
    private static final String CHANNEL_NAME = "Channel";
    private static final String CHANNEL_DESCRIPTION = "test messages";

    @BeforeEach
    @Transactional
        //테스트 전 생성
    void setUp() throws Exception {
        testUserId = createTestUser();
        testChannelId = createTestChannel();
    }


    private String createTestUser() throws Exception {
        UserCreateRequest userCreateRequest = new UserCreateRequest(USERNAME, EMAIL,
            PASSWORD);
        MockMultipartFile userRequestJson = new MockMultipartFile(
            "userCreateRequest",
            "testUserCreateRequest.json",
            "application/json",
            objectMapper.writeValueAsBytes(userCreateRequest)
        );

        MvcResult userResult = mockMvc.perform(multipart("/api/users")
                .file(userRequestJson))
            .andExpect(status().isCreated())
            .andReturn();

        UserDto userDto = objectMapper.readValue(
            userResult.getResponse().getContentAsString(),
            UserDto.class
        );

        return userDto.id().toString();
    }


    private String createTestChannel() throws Exception {
        PublicChannelCreateRequest channelCreateRequest = new PublicChannelCreateRequest(
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION
        );

        MvcResult channelResult = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(channelCreateRequest)))
            .andExpect(status().isCreated())
            .andReturn();

        ChannelDto channelDto = objectMapper.readValue(
            channelResult.getResponse().getContentAsString(),
            ChannelDto.class
        );

        return channelDto.id().toString();
    }

    private String createTestMessage(String content) throws Exception {
        MessageCreateRequest createRequest = new MessageCreateRequest(
            content,
            UUID.fromString(testChannelId),
            UUID.fromString(testUserId)
        );

        MockMultipartFile requestJson = new MockMultipartFile(
            "messageCreateRequest",
            "testMessageCreateRequest.json",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );

        MvcResult result = mockMvc.perform(multipart("/api/messages")
                .file(requestJson)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        return responseJson.get("id").asText();
    }

    @Test
    @DisplayName("메시지 생성 성공 - 텍스트만")
    @Transactional
    void createMessage_Success_TextOnly() throws Exception {
        MessageCreateRequest createRequest = new MessageCreateRequest(
            MESSAGE,
            UUID.fromString(testChannelId),
            UUID.fromString(testUserId)
        );

        MockMultipartFile messageIncludeFile = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );

        ResultActions result = mockMvc.perform(multipart("/api/messages")
            .file(messageIncludeFile)
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.content").value(MESSAGE))
            .andExpect(jsonPath("$.channelId").value(testChannelId))
            .andExpect(jsonPath("$.author.id").value(testUserId))
            .andExpect(jsonPath("$.attachments").isEmpty());
    }

    @Test
    @DisplayName("메시지 생성 성공 - 첨부파일 포함")
    @Transactional
    void createMessage_Success_WithAttachment() throws Exception {
        String messageContent = "Image attached";
        String attachmentName = "test.txt";
        String attachmentContent = "test content";
        UUID attachmentId = UUID.randomUUID();

        MessageCreateRequest createRequest = new MessageCreateRequest(
            messageContent,
            UUID.fromString(testChannelId),
            UUID.fromString(testUserId)
        );

        MockMultipartFile requestJson = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );

        MockMultipartFile attachment = new MockMultipartFile(
            "attachments",
            attachmentName,
            MediaType.TEXT_PLAIN_VALUE,
            attachmentContent.getBytes()
        );

        // Mocking BinaryContentService
        BinaryContentDto mockAttachmentDto = new BinaryContentDto(
            attachmentId,
            attachmentName,
            (long) attachmentContent.getBytes().length,
            MediaType.TEXT_PLAIN_VALUE
        );
        when(binaryContentService.create(any(MockMultipartFile.class))).thenReturn(mockAttachmentDto);

        // Mocking BinaryContentRepository
        BinaryContent mockPersistedBinaryContent = Mockito.mock(BinaryContent.class);
        when(mockPersistedBinaryContent.getId()).thenReturn(attachmentId);
        when(mockPersistedBinaryContent.getFileName()).thenReturn(attachmentName);
        when(mockPersistedBinaryContent.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);
        when(mockPersistedBinaryContent.getSize()).thenReturn((long) attachmentContent.getBytes().length);
        when(mockPersistedBinaryContent.getCreatedAt()).thenReturn(Instant.now());

        when(binaryContentRepository.findById(eq(attachmentId))).thenReturn(Optional.of(mockPersistedBinaryContent));

        ResultActions result = mockMvc.perform(multipart("/api/messages")
            .file(requestJson)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.content").value(messageContent))
            .andExpect(jsonPath("$.attachments", hasSize(1)))
            .andExpect(jsonPath("$.attachments[0].fileName").value(attachmentName));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 채널 찾을 수 없음")
    @Transactional
    void createMessage_Fail_ChannelNotFound() throws Exception {
        String nonExistentChannelId = UUID.randomUUID().toString();
        MessageCreateRequest createRequest = new MessageCreateRequest(
            "Test",
            UUID.fromString(nonExistentChannelId),
            UUID.fromString(testUserId)
        );

        MockMultipartFile requestJson = new MockMultipartFile(
            "messageCreateRequest",
            "testMessageCreateRequest.json",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );

        ResultActions result = mockMvc.perform(multipart("/api/messages")
            .file(requestJson)
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."))
            .andExpect(jsonPath("$.details.channelId").value(nonExistentChannelId));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 작성자 찾을 수 없음")
    @Transactional
    void createMessage_Fail_AuthorNotFound() throws Exception {
        String nonExistentAuthorId = UUID.randomUUID().toString();
        MessageCreateRequest createRequest = new MessageCreateRequest(
            "Test Message",
            UUID.fromString(testChannelId),
            UUID.fromString(nonExistentAuthorId)
        );

        MockMultipartFile requestJson = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );

        ResultActions result = mockMvc.perform(multipart("/api/messages")
            .file(requestJson)
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.details.userId").value(nonExistentAuthorId));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 첨부파일 업로드 오류")
    @Transactional
    void createMessage_Fail_AttachmentError() throws Exception {
        MessageCreateRequest createRequest = new MessageCreateRequest(
            "Message with failing attachment",
            UUID.fromString(testChannelId),
            UUID.fromString(testUserId)
        );
        MockMultipartFile requestJson = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );
        MockMultipartFile attachment = new MockMultipartFile(
            "attachments",
            "error_file.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "error content".getBytes()
        );

        Map<String, Object> expectedDetails = Map.of("operation", "save-file", "fileName",
            "error_file.txt");
        when(binaryContentService.create(attachment)).thenThrow(
            new FileProcessingCustomException(expectedDetails));

        ResultActions result = mockMvc.perform(multipart("/api/messages")
            .file(requestJson)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(ErrorCode.FILE_PROCESSING_ERROR.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.FILE_PROCESSING_ERROR.getMessage()))
            .andExpect(jsonPath("$.details.operation").value("save-file"))
            .andExpect(jsonPath("$.details.fileName").value("error_file.txt"));
    }

    @Test
    @DisplayName("메시지 수정 성공")
    @Transactional
    void updateMessage_Success() throws Exception {
        String content = "Message";
        String updatedContent = "Updated Message";
        String messageId = createTestMessage(content);

        MessageUpdateRequest updateRequest = new MessageUpdateRequest(updatedContent);

        ResultActions result = mockMvc.perform(patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(messageId))
            .andExpect(jsonPath("$.content").value(updatedContent));
    }

    @Test
    @DisplayName("메시지 수정 실패 - 메시지 찾을 수 없음")
    @Transactional
    void updateMessage_Fail_NotFound() throws Exception {
        String nonExistentMessageId = UUID.randomUUID().toString();
        MessageUpdateRequest updateRequest = new MessageUpdateRequest("Update non-existent");

        ResultActions result = mockMvc.perform(patch("/api/messages/" + nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.MESSAGE_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.details.messageId").value(nonExistentMessageId));
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    @Transactional
    void deleteMessage_Success() throws Exception {
        String messageContent = "Message to delete";
        String messageId = createTestMessage(messageContent);

        ResultActions result = mockMvc.perform(delete("/api/messages/" + messageId));

        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("메시지 삭제 실패 - 메시지 찾을 수 없음")
    @Transactional
    void deleteMessage_Fail_NotFound() throws Exception {
        String nonExistentMessageId = UUID.randomUUID().toString();

        ResultActions result = mockMvc.perform(delete("/api/messages/" + nonExistentMessageId));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.MESSAGE_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.details.messageId").value(nonExistentMessageId));
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 성공")
    @Transactional
    void findAllMessagesByChannelId_Success() throws Exception {
        String message1 = "First message";
        String message2 = "Second message";
        createTestMessage(message1);
        createTestMessage(message2);

        ResultActions result = mockMvc.perform(get("/api/messages")
            .param("channelId", testChannelId)
            .param("size", "10"));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[*].content", containsInAnyOrder(message1, message2)))
            .andExpect(jsonPath("$.hasNext").isBoolean())
            .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 실패 - 채널 찾을 수 없음")
    @Transactional
    void findAllMessagesByChannelId_Fail_ChannelNotFound() throws Exception {
        String nonExistentChannelId = UUID.randomUUID().toString();

        ResultActions result = mockMvc.perform(
            get("/api/messages").param("channelId", nonExistentChannelId));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.CHANNEL_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.details.channelId").value(nonExistentChannelId));
    }
}
