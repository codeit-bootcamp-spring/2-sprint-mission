package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageService messageService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Create Message_성공")
  void createMessage_Success() throws Exception {
    // given
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.createPublicChannel(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );
    UserDto user = userService.createUser(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest(
        "테스트 메시지 내용입니다.",
        channel.id(),
        user.id()
    );
    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createRequest)
    );
    MockMultipartFile attachmentPart = new MockMultipartFile(
        "attachments",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "테스트 첨부 파일 내용".getBytes()
    );

    // when, then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart)
            .file(attachmentPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.content").value("테스트 메시지 내용입니다."))
        .andExpect(jsonPath("$.channelId").value(channel.id().toString()))
        .andExpect(jsonPath("$.author.id").value(user.id().toString()))
        .andExpect(jsonPath("$.attachments", hasSize(1)))
        .andExpect(jsonPath("$.attachments[0].fileName").value("test.txt"));
  }

  @Test
  @DisplayName("Create Message_실패_유효하지 않은 요청 데이터")
  void createMessage_Failure_InvalidRequest() throws Exception {
    // given
    MessageCreateRequest invalidRequest = new MessageCreateRequest(
        "", // 내용이 비어있음
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    // when, then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("findAllMessagesByChannelId_성공")
  void findAllMessagesByChannelId_Success() throws Exception {
    // given
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.createPublicChannel(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );
    UserDto user = userService.createUser(userRequest, Optional.empty());

    MessageCreateRequest messageRequest1 = new MessageCreateRequest(
        "첫 번째 메시지 내용입니다.",
        channel.id(),
        user.id()
    );
    MessageCreateRequest messageRequest2 = new MessageCreateRequest(
        "두 번째 메시지 내용입니다.",
        channel.id(),
        user.id()
    );
    messageService.createMessage(messageRequest1, new ArrayList<>());
    messageService.createMessage(messageRequest2, new ArrayList<>());

    // when, then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].content").value("두 번째 메시지 내용입니다."))
        .andExpect(jsonPath("$.content[1].content").value("첫 번째 메시지 내용입니다."))
        .andExpect(jsonPath("$.size").exists())
        .andExpect(jsonPath("$.hasNext").exists())
        .andExpect(jsonPath("$.totalElements").isEmpty());
  }

  @Test
  @DisplayName("Update Message_성공")
  void updateMessage_Success() throws Exception {
    // given
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.createPublicChannel(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );
    UserDto user = userService.createUser(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest(
        "원본 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    MessageDto createdMessage = messageService.createMessage(createRequest, new ArrayList<>());
    UUID messageId = createdMessage.id();

    // 메시지 업데이트 요청
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    // when, then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("수정된 메시지 내용입니다."))
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("Update Message_실패_존재하지 않는 메시지")
  void updateMessage_Failure_MessageNotFound() throws Exception {
    // given
    UUID nonExistentMessageId = UUID.randomUUID();
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    // when, then
    mockMvc.perform(patch("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("메시지를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
  }

  @Test
  @DisplayName("Delete Message_성공")
  void deleteMessage_success() throws Exception {
    // given
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.createPublicChannel(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );
    UserDto user = userService.createUser(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest(
        "삭제할 메시지 내용입니다.",
        channel.id(),
        user.id()
    );
    MessageDto createdMessage = messageService.createMessage(createRequest, new ArrayList<>());
    UUID messageId = createdMessage.id();

    // when, then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @Test
  @DisplayName("Delete Message_실패_존재하지 않는 ID")
  void deleteMessage_fail_notFound() throws Exception {
    // given: DB에 없는 UUID 생성
    UUID invalidMessageId = UUID.randomUUID();

    // when, then
    mockMvc.perform(delete("/api/messages/{messageId}", invalidMessageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("메시지를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
  }

}
