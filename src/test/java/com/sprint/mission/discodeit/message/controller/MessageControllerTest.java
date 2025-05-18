package com.sprint.mission.discodeit.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.service.MessageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("메세지 채널아이디, 입력한 유저아이디, 내용, 첨부파일들을 입력하면, 메세지를 생성합니다.")
    @Test
    void create() {
        // given
        String uniqueContent = UUID.randomUUID().toString();
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

        Message message = new Message(channel, null, uniqueContent, List.of());
        UUID messageFakeId = UUID.randomUUID();
        ReflectionTestUtils.setField(message, "id", messageFakeId);
        MessageResult stubResult = MessageResult.fromEntity(message, null, List.of());

        BDDMockito.given(messageService.create(any(), any())).willReturn(stubResult);
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(uniqueContent, UUID.randomUUID(), UUID.randomUUID());
        MockMultipartFile multipartFileMessageCreateRequest = new MockMultipartFile("messageCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                convertJsonRequest(messageCreateRequest).getBytes());
        MockMultipartFile multipartBinaryFile = new MockMultipartFile("attachments", null, MediaType.APPLICATION_OCTET_STREAM_VALUE,
                convertJsonRequest(List.of()).getBytes());

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/messages")
                        .multipart()
                        .file(multipartFileMessageCreateRequest)
                        .file(multipartBinaryFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(messageFakeId.toString());
    }

    @DisplayName("이미지 파일 리스트가 입력받지 않으면, 404 예외를 반환합니다.")
    @Test
    void create_NoMultipartFile() {
        // given
        String uniqueContent = UUID.randomUUID().toString();
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

        Message message = new Message(channel, null, uniqueContent, List.of());
        UUID fakeId = UUID.randomUUID();
        ReflectionTestUtils.setField(message, "id", fakeId);
        MessageResult stubResult = MessageResult.fromEntity(message, null, List.of());
        BDDMockito.given(messageService.create(any(), any())).willReturn(stubResult);
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(uniqueContent, UUID.randomUUID(), UUID.randomUUID());
        MockMultipartFile multipartFileMessageCreateRequest = new MockMultipartFile("messageCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                convertJsonRequest(messageCreateRequest).getBytes());

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/messages")
                        .multipart()
                        .file(multipartFileMessageCreateRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatus4xxClientError();
    }

    private String convertJsonRequest(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 중 오류 발생", e);
        }
    }

}