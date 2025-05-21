package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ChannelService channelService;
  @Autowired
  private UserService userService;

  @Test
  @DisplayName("공개 채널 생성 성공")
  void createPublicChannelSuccess() throws Exception {
    PublicChannelCreateRequest createRequest =
        new PublicChannelCreateRequest("Test Channel", "This is a test channel description.");
    String requestBody = objectMapper.writeValueAsString(createRequest);

    mockMvc.perform(
            post("/api/channels/public").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type", is(ChannelType.PUBLIC.name())))
        .andExpect(jsonPath("$.name", is("Test Channel")))
        .andExpect(jsonPath("$.description", is("This is a test channel description.")));
  }

  @Test
  @DisplayName("공개 채널 생성 실패 - 잘못된 요청")
  void createPublicChannelFailure_invalidRequest() throws Exception {
    PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest("a",
        "This is a test channel description.");
    String requestBody = objectMapper.writeValueAsString(invalidRequest);

    mockMvc.perform(
            post("/api/channels/public").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("비공개 채널 생성 성공")
  void createPrivateChannelSuccess() throws Exception {
    UserCreateRequest userRequest1 = new UserCreateRequest("user1", "user1@example.com",
        "Password1!");
    UserCreateRequest userRequest2 = new UserCreateRequest("user2", "user2@example.com",
        "Password1!");

    UserDto user1 = userService.create(userRequest1, Optional.empty());
    UserDto user2 = userService.create(userRequest2, Optional.empty());

    List<UUID> participantIds = List.of(user1.id(), user2.id());
    PrivateChannelCreateRequest createRequest = new PrivateChannelCreateRequest(participantIds);
    String requestBody = objectMapper.writeValueAsString(createRequest);

    mockMvc.perform(
            post("/api/channels/private").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type", is(ChannelType.PRIVATE.name())))
        .andExpect(jsonPath("$.participants", hasSize(2)));
  }

  @Test
  @DisplayName("채널 수정 성공")
  void updateChannelSuccess() throws Exception {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("Original Channel",
        "Original description.");
    ChannelDto createdChannel = channelService.create(createRequest);
    UUID channelId = createdChannel.id();

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("Updated Channel",
        "Updated description.");
    String requestBody = objectMapper.writeValueAsString(updateRequest);

    mockMvc.perform(
            patch("/api/channels/{channelId}", channelId).contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(channelId.toString())))
        .andExpect(jsonPath("$.name", is("Updated Channel")))
        .andExpect(jsonPath("$.description", is("Updated description.")));
  }

  @Test
  @DisplayName("채널 수정 실패 - 채널 없음")
  void updateChannelFailure_channelNotFound() throws Exception {
    UUID nonExistentChannelId = UUID.randomUUID();

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("Updated Channel",
        "Updated description.");
    String requestBody = objectMapper.writeValueAsString(updateRequest);

    mockMvc.perform(patch("/api/channels/{channelId}", nonExistentChannelId).contentType(
            MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannelSuccess() throws Exception {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("Channel to delete",
        "Description to delete.");
    ChannelDto createdChannel = channelService.create(createRequest);
    UUID channelId = createdChannel.id();

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    UserCreateRequest userRequest = new UserCreateRequest("testuser", "testuser@example.com",
        "Password1!");
    UserDto user = userService.create(userRequest, Optional.empty());

    mockMvc.perform(get("/api/channels").param("userId", user.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + channelId + "')]").doesNotExist());
  }

  @Test
  @DisplayName("채널 삭제 실패 - 채널 없음")
  void deleteChannelFailure_channelNotFound() throws Exception {
    UUID nonExistentChannelId = UUID.randomUUID();

    mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId))
        .andExpect(status().isNotFound());
  }
}