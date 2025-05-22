package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
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
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  public void createPublicChannel_Success() throws Exception {
    PublicChannelRequest request = new PublicChannelRequest("publicChannel", "publicChannel");
    UUID channelId = UUID.randomUUID();
    ChannelDto expected = new ChannelDto(channelId, ChannelType.PUBLIC, "publicChannel",
        "publicChannel", new ArrayList<>(), null);

    given(channelService.create(eq(request))).willReturn(expected);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("publicChannel"));
  }

  @Test
  void createPublicChannel_Failure_InvalidRequest() throws Exception {
    PublicChannelRequest invalidRequest = new PublicChannelRequest(
        "",
        "publicChannel"
    );

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createPrivateChannel_Success() throws Exception {
    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();
    List<UUID> participantIds = List.of(user1Id, user2Id);
    PrivateChannelRequest createRequest = new PrivateChannelRequest(participantIds);

    UUID channelId = UUID.randomUUID();
    List<UserDto> participants = new ArrayList<>();

    participants.add(new UserDto(user1Id, "user1", "user1@gmail.com", null, false));
    participants.add(new UserDto(user2Id, "user2", "user2@gmail.com", null, false));

    ChannelDto expected = new ChannelDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        participants,
        null
    );

    given(channelService.create(any(PrivateChannelRequest.class)))
        .willReturn(expected);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants").isArray())
        .andExpect(jsonPath("$.participants.length()").value(2));
  }

  @Test
  void deleteChannel_Success() throws Exception {
    UUID channelId = UUID.randomUUID();
    willDoNothing().given(channelService).delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteChannel_Failure_ChannelNotFound() throws Exception {
    UUID invalidChannelId = UUID.randomUUID();
    ChannelNotFoundException ex = new ChannelNotFoundException().notFoundWithId(
        invalidChannelId);
    willThrow(ex).given(channelService).delete(invalidChannelId);

    mockMvc.perform(delete("/api/channels/{channelId}", invalidChannelId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateChannel_Success() throws Exception {
    ChannelUpdateRequest request = new ChannelUpdateRequest(
        "update",
        "update"
    );
    UUID channelId = UUID.randomUUID();

    ChannelDto updatedChannel = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "update",
        "update",
        new ArrayList<>(),
        null
    );

    given(channelService.update(eq(channelId), any(ChannelUpdateRequest.class)))
        .willReturn(updatedChannel);

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("update"))
        .andExpect(jsonPath("$.description").value("update"));
  }

  @Test
  void updateChannel_Failure_PrivateChannelUpdate() throws Exception {
    ChannelUpdateRequest request = new ChannelUpdateRequest(
        "update",
        "update"
    );
    UUID privateChannelId = UUID.randomUUID();
    PrivateChannelUpdateException ex = new PrivateChannelUpdateException().privateChannelUpdateWithId(
        privateChannelId);

    given(channelService.update(eq(privateChannelId), any(ChannelUpdateRequest.class)))
        .willThrow(ex);

    mockMvc.perform(patch("/api/channels/{channelId}", privateChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void findAllByUserId_Success() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();
    UserDto userDto = new UserDto(
        userId, "user1", "user1@example.com", null, true);

    List<ChannelDto> channelDtoList = List.of(
        new ChannelDto(
            channelId1, ChannelType.PUBLIC, "channel1", "channel1",
            new ArrayList<>(), Instant.now()),
        new ChannelDto(
            channelId2, ChannelType.PRIVATE, null, null,
            List.of(userDto), Instant.now())
    );

    given(channelService.findAllByUserId(userId)).willReturn(channelDtoList);

    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(channelId1.toString()))
        .andExpect(jsonPath("$[0].type").value(ChannelType.PUBLIC.name()))
        .andExpect(jsonPath("$[0].name").value("channel1"))
        .andExpect(jsonPath("$[1].id").value(channelId2.toString()))
        .andExpect(jsonPath("$[1].type").value(ChannelType.PRIVATE.name()));
  }
}
