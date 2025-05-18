package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
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

    // When & Then
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
  void updateChannel_Success() throws Exception {
    // Given
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

    // When & Then
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
    // Given
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
}
