package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Create Public Channel_성공")
  void createPublicChannel_Success() throws Exception {
    // given
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    // when, then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.name()))
        .andExpect(jsonPath("$.name").value("테스트 채널"))
        .andExpect(jsonPath("$.description").value("테스트 채널 설명입니다."));
  }

  @Test
  @DisplayName("Create Public Channel_실패_유효하지 않은 요청 데이터")
  void createPublicChannel_Failure_InvalidRequest() throws Exception {
    // given
    PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest(
        "a", // 최소 길이 위반
        "테스트 채널 설명입니다."
    );

    // when, Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Create Private Channel_성공")
  void createPrivateChannel_Success() throws Exception {
    // given
    UserCreateRequest userRequest1 = new UserCreateRequest(
        "user1",
        "user1@example.com",
        "Password1!"
    );

    UserCreateRequest userRequest2 = new UserCreateRequest(
        "user2",
        "user2@example.com",
        "Password1!"
    );

    UserDto user1 = userService.createUser(userRequest1, Optional.empty());
    UserDto user2 = userService.createUser(userRequest2, Optional.empty());

    List<UUID> participantIds = List.of(user1.id(), user2.id());
    PrivateChannelCreateRequest createRequest = new PrivateChannelCreateRequest(participantIds);

    // when, then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type").value(ChannelType.PRIVATE.name()))
        .andExpect(jsonPath("$.participants", hasSize(2)));
  }

  @Test
  @DisplayName("findAllChannelsByUserId_성공")
  void findAllChannelsByUserId_Success() throws Exception {
    // given
    UserCreateRequest userRequest = new UserCreateRequest(
        "channeluser",
        "channeluser@example.com",
        "Password1!"
    );

    UserDto user = userService.createUser(userRequest, Optional.empty());
    UUID userId = user.id();

    // 공개 채널 생성
    PublicChannelCreateRequest publicChannelRequest = new PublicChannelCreateRequest(
        "공개 채널 1",
        "공개 채널 설명입니다."
    );

    channelService.createPublicChannel(publicChannelRequest);

    // 비공개 채널 생성
    UserCreateRequest otherUserRequest = new UserCreateRequest(
        "otheruser",
        "otheruser@example.com",
        "Password1!"
    );

    UserDto otherUser = userService.createUser(otherUserRequest, Optional.empty());

    PrivateChannelCreateRequest privateChannelRequest = new PrivateChannelCreateRequest(
        List.of(userId, otherUser.id())
    );

    channelService.createPrivateChannel(privateChannelRequest);

    // when, then
    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].type").value(ChannelType.PUBLIC.name()))
        .andExpect(jsonPath("$[1].type").value(ChannelType.PRIVATE.name()));
  }

  @Test
  @DisplayName("Update Channel_성공")
  void update_Success() throws Exception {
    // given
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "원본 채널",
        "원본 채널 설명입니다."
    );

    ChannelDto createdChannel = channelService.createPublicChannel(createRequest);

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
        "수정된 채널 이름", "수정 테스트입니다."
    );

    // when, then
    mockMvc.perform(patch("/api/channels/{channelId}", createdChannel.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(createdChannel.id().toString()))
        .andExpect(jsonPath("$.name").value("수정된 채널 이름"))
        .andExpect(jsonPath("$.description").value("수정 테스트입니다."));
  }

  @Test
  @DisplayName("Update Channel_실패_id가 유효하지 않을 때")
  void update_Failure_WhenInvalidId() throws Exception {
    // given
    UUID invalidChannelId = UUID.randomUUID();
    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
        "수정된 채널 이름", "수정 테스트입니다."
    );

    // when, then
    mockMvc.perform(patch("/api/channels/{channelId}", invalidChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
  }

  @Test
  @DisplayName("delete Channel_성공")
  void deleteChannel_Success() throws Exception {
    // Given
    // 공개 채널 생성
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "삭제할 채널",
        "삭제할 채널 설명입니다."
    );

    ChannelDto createdChannel = channelService.createPublicChannel(createRequest);
    UUID channelId = createdChannel.id();

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // 삭제 확인 - 사용자로 채널 조회 시 삭제된 채널은 조회되지 않아야 함
    UserCreateRequest userRequest = new UserCreateRequest(
        "testuser",
        "testuser@example.com",
        "Password1!"
    );

    UserDto user = userService.createUser(userRequest, Optional.empty());

    mockMvc.perform(get("/api/channels")
            .param("userId", user.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + channelId + "')]").doesNotExist());
  }

  @Test
  @DisplayName("delete Channel_실패_존재하지 않는 채널")
  void deleteChannel_Failure_WhenChannelNotFound() throws Exception {
    // Given
    UUID nonExistentChannelId = UUID.randomUUID();

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId))
        .andExpect(status().isNotFound());
  }

}
