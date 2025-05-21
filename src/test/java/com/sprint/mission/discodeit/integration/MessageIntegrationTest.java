package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
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
class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MessageService messageService;
  @Autowired
  private ChannelService channelService;
  @Autowired
  private UserService userService;

  @Test
  @DisplayName("메시지 생성 성공 테스트")
  void createMessageSuccess() throws Exception {
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("Test Channel",
        "Test channel description.");
    ChannelDto channel = channelService.create(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest("messageuser", "messageuser@example.com",
        "Password1!");
    UserDto user = userService.create(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest("This is a test message content.",
        channel.id(), user.id());

    MockMultipartFile messageCreateRequestPart =
        new MockMultipartFile("messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(createRequest));

    MockMultipartFile attachmentPart =
        new MockMultipartFile("attachments", "test.txt", MediaType.TEXT_PLAIN_VALUE,
            "Test attachment content".getBytes());

    mockMvc.perform(multipart("/api/messages").file(messageCreateRequestPart).file(attachmentPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.content", is("This is a test message content.")))
        .andExpect(jsonPath("$.channelId", is(channel.id().toString())))
        .andExpect(jsonPath("$.author.id", is(user.id().toString())))
        .andExpect(jsonPath("$.attachments", hasSize(1)))
        .andExpect(jsonPath("$.attachments[0].fileName", is("test.txt")));
  }

  @Test
  @DisplayName("메시지 생성 실패 테스트 - 잘못된 요청")
  void createMessageFailure_invalidRequest() throws Exception {
    MessageCreateRequest invalidRequest = new MessageCreateRequest("", UUID.randomUUID(),
        UUID.randomUUID());

    MockMultipartFile messageCreateRequestPart =
        new MockMultipartFile("messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(invalidRequest));

    mockMvc.perform(multipart("/api/messages").file(messageCreateRequestPart))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("메시지 수정 성공 테스트")
  void updateMessageSuccess() throws Exception {
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("Test Channel",
        "Test channel description.");
    ChannelDto channel = channelService.create(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest("messageuser", "messageuser@example.com",
        "Password1!");
    UserDto user = userService.create(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest("원본 메시지 내용", channel.id(),
        user.id());
    MessageDto createdMessage = messageService.create(createRequest, new ArrayList<>());

    MessageUpdateRequest updateRequest = new MessageUpdateRequest("수정된 메시지 내용");

    String body = objectMapper.writeValueAsString(updateRequest);

    mockMvc.perform(patch("/api/messages/{messageId}", createdMessage.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(createdMessage.id().toString())))
        .andExpect(jsonPath("$.content", is("수정된 메시지 내용")))
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("메시지 수정 실패 테스트 - 메시지 없음")
  void updateMessageFailure_messageNotFound() throws Exception {
    UUID fakeId = UUID.randomUUID();
    MessageUpdateRequest updateRequest = new MessageUpdateRequest("수정된 메시지 내용");

    String body = objectMapper.writeValueAsString(updateRequest);

    mockMvc.perform(patch("/api/messages/{messageId}", fakeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("메시지 삭제 성공 테스트")
  void deleteMessageSuccess() throws Exception {
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("Test Channel",
        "Test channel description.");
    ChannelDto channel = channelService.create(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest("messageuser", "messageuser@example.com",
        "Password1!");
    UserDto user = userService.create(userRequest, Optional.empty());

    MessageCreateRequest createRequest = new MessageCreateRequest("삭제할 메시지 내용", channel.id(),
        user.id());
    MessageDto createdMessage = messageService.create(createRequest, new ArrayList<>());

    mockMvc.perform(delete("/api/messages/{messageId}", createdMessage.id()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @Test
  @DisplayName("메시지 삭제 실패 테스트 - 메시지 없음")
  void deleteMessageFailure_messageNotFound() throws Exception {
    UUID fakeId = UUID.randomUUID();

    mockMvc.perform(delete("/api/messages/{messageId}", fakeId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 성공 테스트")
  void findAllMessagesByChannelIdSuccess() throws Exception {
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("Test Channel",
        "Test channel description.");
    ChannelDto channel = channelService.create(channelRequest);

    UserCreateRequest userRequest = new UserCreateRequest("messageuser", "messageuser@example.com",
        "Password1!");
    UserDto user = userService.create(userRequest, Optional.empty());

    MessageCreateRequest msgRequest1 = new MessageCreateRequest("첫 번째 메시지입니다.", channel.id(),
        user.id());
    MessageCreateRequest msgRequest2 = new MessageCreateRequest("두 번째 메시지입니다.", channel.id(),
        user.id());

    messageService.create(msgRequest1, new ArrayList<>());
    messageService.create(msgRequest2, new ArrayList<>());

    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].content", is("두 번째 메시지입니다.")))
        .andExpect(jsonPath("$.content[1].content", is("첫 번째 메시지입니다.")))
        .andExpect(jsonPath("$.size").exists())
        .andExpect(jsonPath("$.hasNext").exists())
        .andExpect(jsonPath("$.totalElements").isEmpty());
  }
}
