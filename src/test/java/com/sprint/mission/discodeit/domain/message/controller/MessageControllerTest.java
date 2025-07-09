package com.sprint.mission.discodeit.domain.message.controller;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.testutil.ControllerTestSupport;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

class MessageControllerTest extends ControllerTestSupport {

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
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(uniqueContent,
        UUID.randomUUID(), UUID.randomUUID());
    MockMultipartFile multipartFileMessageCreateRequest = new MockMultipartFile(
        "messageCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
        convertJsonRequest(messageCreateRequest).getBytes());
    MockMultipartFile multipartBinaryFile = new MockMultipartFile("attachments", null,
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        convertJsonRequest(List.of()).getBytes());

    // when & then
    Assertions.assertThat(mockMvc.post()
            .uri("/api/messages")
            .multipart()
            .file(multipartFileMessageCreateRequest)
            .file(multipartBinaryFile)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .hasStatusOk()
        .bodyJson()
        .extractingPath("$.id")
        .isEqualTo(messageFakeId.toString());
  }

  private String convertJsonRequest(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JSON 직렬화 중 오류 발생", e);
    }
  }

}