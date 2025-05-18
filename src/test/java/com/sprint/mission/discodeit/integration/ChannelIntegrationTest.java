package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  private final String name = "channel";
  private final String description = "channel description";

  @Test
  public void createPublicChannel_Success() throws Exception {
    PublicChannelRequest request = new PublicChannelRequest(name, description);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("channel"));
  }

  @Test
  void createPublicChannel_Failure_InvalidRequest() throws Exception {
    PublicChannelRequest invalidRequest = new PublicChannelRequest("", description);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createPrivateChannel_Success() throws Exception {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("user", "user@gmail.com",
        "user1234");
    UserCreateRequest userCreateRequest2 = new UserCreateRequest("user2", "user2@gmail.com",
        "user1234");

    UserDto user1 = userService.create(userCreateRequest1, null);
    UserDto user2 = userService.create(userCreateRequest2, null);

    PrivateChannelRequest request = new PrivateChannelRequest(List.of(user1.id(), user2.id()));

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants").isArray())
        .andExpect(jsonPath("$.participants.length()").value(2));
  }

  @Test
  void deleteUser_Success() throws Exception {
    PublicChannelRequest request = new PublicChannelRequest(name, description);
    ChannelDto channelDto = channelService.create(request);

    mockMvc.perform(delete("/api/channels/{channelId}", channelDto.id()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteChannel_Failure_ChannelNotFound() throws Exception {
    mockMvc.perform(delete("/api/channels/{channelId}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateChannel_Success() throws Exception {
    PublicChannelRequest createRequest = new PublicChannelRequest(name, description);
    ChannelDto channelDto = channelService.create(createRequest);

    ChannelUpdateRequest request = new ChannelUpdateRequest(
        "newChannel", "newChannelDescription");

    mockMvc.perform(patch("/api/channels/{channelId}", channelDto.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("newChannel"))
        .andExpect(jsonPath("$.description").value("newChannelDescription"));
  }

  @Test
  void updateChannel_Failure_PrivateChannelUpdate() throws Exception {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("user", "user@gmail.com",
        "user1234");
    UserDto user1 = userService.create(userCreateRequest1, null);
    PrivateChannelRequest privateChannel = new PrivateChannelRequest(List.of(user1.id()));
    ChannelDto channelDto = channelService.create(privateChannel);

    ChannelUpdateRequest request = new ChannelUpdateRequest(
        "newChannel", "newChannelDescription");

    mockMvc.perform(patch("/api/channels/{channelId}", channelDto.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void findAllByUserId_Success() throws Exception {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("user", "user@gmail.com",
        "user1234");
    UserDto user = userService.create(userCreateRequest1, null);
    UUID userId = user.id();

    PublicChannelRequest createRequest1 = new PublicChannelRequest(name, description);
    channelService.create(createRequest1);
    PublicChannelRequest createRequest2 = new PublicChannelRequest(name, description);
    channelService.create(createRequest2);

    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].type").value(ChannelType.PUBLIC.name()))
        .andExpect(jsonPath("$[1].type").value(ChannelType.PUBLIC.name()));
  }
}