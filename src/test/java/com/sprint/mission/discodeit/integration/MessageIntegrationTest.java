package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.UUID;
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
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class MessageIntegrationTest {

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

  private final String content = "message";

  @Test
  void createMessage_Success() throws Exception {
    UserCreateRequest userRequest = new UserCreateRequest(
        "user", "user@gmail.com", "user1234");
    UserDto user = userService.create(userRequest, null);

    PublicChannelRequest channelRequest = new PublicChannelRequest(
        "channel", "channel description");
    ChannelDto channel = channelService.create(channelRequest);

    MessageCreateRequest request = new MessageCreateRequest(
        content, channel.id(), user.id());

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );
    MockMultipartFile attachmentPart = new MockMultipartFile(
        "attachments",
        "human.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy-image-bytes".getBytes()
    );

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .file(attachmentPart))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.content").value(content))
        .andExpect(jsonPath("$.channelId").value(channel.id().toString()))
        .andExpect(jsonPath("$.author.id").value(user.id().toString()))
        .andExpect(jsonPath("$.attachments[0].fileName").value("human.jpg"));
  }

  @Test
  void createMessage_Failure_InvalidRequest() throws Exception {
    MessageCreateRequest request = new MessageCreateRequest(
        "",
        null,
        null
    );

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateMessage_Success() throws Exception {
    UserCreateRequest userRequest = new UserCreateRequest(
        "user", "user@gmail.com", "user1234");
    UserDto userDto = userService.create(userRequest, null);

    PublicChannelRequest channelRequest = new PublicChannelRequest(
        "channel", "channel description");
    ChannelDto channelDto = channelService.create(channelRequest);

    MessageCreateRequest request = new MessageCreateRequest(
        content, channelDto.id(), userDto.id());

    MessageDto messageDto = messageService.create(request, new ArrayList<>());
    UUID messageId = messageDto.id();

    MessageUpdateRequest updateRequest = new MessageUpdateRequest("updatedMessage");

    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.updatedAt").exists())
        .andExpect(jsonPath("$.content").value("updatedMessage"));
  }

  @Test
  void updateMessage_Failure_MessageNotFound() throws Exception {
    MessageUpdateRequest updateRequest = new MessageUpdateRequest("updatedMessage");

    mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteMessage_Success() throws Exception {
    UserCreateRequest userRequest = new UserCreateRequest(
        "user", "user@gmail.com", "user1234");
    UserDto userDto = userService.create(userRequest, null);

    PublicChannelRequest channelRequest = new PublicChannelRequest(
        "channel", "channel description");
    ChannelDto channelDto = channelService.create(channelRequest);

    MessageCreateRequest request = new MessageCreateRequest(
        content, channelDto.id(), userDto.id());

    MessageDto messageDto = messageService.create(request, new ArrayList<>());
    UUID messageId = messageDto.id();

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteChannel_Failure_ChannelNotFound() throws Exception {
    mockMvc.perform(delete("/api/messages/{messageId}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void findAllByChannelId_Success() throws Exception {
    UserCreateRequest userRequest = new UserCreateRequest(
        "user", "user@gmail.com", "user1234");
    UserDto userDto = userService.create(userRequest, null);

    PublicChannelRequest channelRequest = new PublicChannelRequest(
        "channel", "channel description");
    ChannelDto channelDto = channelService.create(channelRequest);

    MessageCreateRequest request1 = new MessageCreateRequest(
        content, channelDto.id(), userDto.id());
    MessageCreateRequest request2 = new MessageCreateRequest(
        "message2", channelDto.id(), userDto.id());
    messageService.create(request1, new ArrayList<>());
    messageService.create(request2, new ArrayList<>());

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelDto.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].content").value("message2"))
        .andExpect(jsonPath("$.content[1].content").value(content))
        .andExpect(jsonPath("$.size").exists())
        .andExpect(jsonPath("$.hasNext").exists())
        .andExpect(jsonPath("$.totalElements").isEmpty());
  }

}
