package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.ChannelMapperImpl;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
@Import(ChannelMapperImpl.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ChannelService channelService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelMapper channelMapper;

  @Test
  @DisplayName("공개 채널 생성 성공")
  void createChannel_public_success() throws Exception {
    // given
    CreatePublicChannelRequestDTO createPublicChannelRequestDTO = new CreatePublicChannelRequestDTO(
        "testChannel", "테스트 채널입니다.");

    UUID channelId = UUID.randomUUID();
    CreatePublicChannelResult createPublicChannelResult = new CreatePublicChannelResult(
        channelId, ChannelType.PUBLIC, createPublicChannelRequestDTO.name(),
        createPublicChannelRequestDTO.description(),
        null,
        Instant.now());

    given(channelService.createPublicChannel(
        channelMapper.toCreatePublicChannelCommand(createPublicChannelRequestDTO)))
        .willReturn(createPublicChannelResult);

    String requestBody = objectMapper.writeValueAsString(createPublicChannelRequestDTO);

    // when + then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("testChannel"))
        .andExpect(jsonPath("$.description").value("테스트 채널입니다."));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 공개 채널 생성 실패")
  void createChannel_public_with_invalidParameter_failed() throws Exception {
    // given
    CreatePublicChannelRequestDTO createPublicChannelRequestDTO = new CreatePublicChannelRequestDTO(
        "", "테스트 채널입니다.");

    UUID channelId = UUID.randomUUID();
    CreatePublicChannelResult createPublicChannelResult = new CreatePublicChannelResult(
        channelId, ChannelType.PUBLIC, createPublicChannelRequestDTO.name(),
        createPublicChannelRequestDTO.description(),
        null,
        Instant.now());

    given(channelService.createPublicChannel(
        channelMapper.toCreatePublicChannelCommand(createPublicChannelRequestDTO)))
        .willReturn(createPublicChannelResult);

    String requestBody = objectMapper.writeValueAsString(createPublicChannelRequestDTO);

    // when + then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("비밀 채널 생성 성공")
  void createChannel_private_success() throws Exception {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO = new CreatePrivateChannelRequestDTO(
        List.of(userId1, userId2));

    UUID channelId = UUID.randomUUID();
    CreatePrivateChannelResult createPrivateChannelResult = new CreatePrivateChannelResult(
        channelId, ChannelType.PRIVATE, null, null,
        List.of(mock(FindUserResult.class), mock(FindUserResult.class)), Instant.now());

    given(channelService.createPrivateChannel(
        channelMapper.toCreatePrivateChannelCommand(createPrivateChannelRequestDTO)))
        .willReturn(createPrivateChannelResult);

    String requestBody = objectMapper.writeValueAsString(createPrivateChannelRequestDTO);

    // when + then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").isEmpty())
        .andExpect(jsonPath("$.description").isEmpty());
  }

  @Test
  @DisplayName("공개 채널 수정성공")
  void updateChannel_public_success() throws Exception {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    UpdateChannelRequestDTO updateChannelRequestDTO = new UpdateChannelRequestDTO(
        "newChannel", "수정된 채널입니다.");

    UUID channelId = UUID.randomUUID();
    UpdateChannelResult updateChannelResult = new UpdateChannelResult(
        channelId, ChannelType.PUBLIC, updateChannelRequestDTO.newName(),
        updateChannelRequestDTO.newDescription(), null, Instant.now());

    given(channelService.update(
        channelId, channelMapper.toUpdateChannelCommand(updateChannelRequestDTO)))
        .willReturn(updateChannelResult);

    String requestBody = objectMapper.writeValueAsString(updateChannelRequestDTO);

    // when + then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("newChannel"))
        .andExpect(jsonPath("$.description").value("수정된 채널입니다."));
  }

  @Test
  @DisplayName("입력값이 잘못 들어왔을 때, 공개 채널 수정실패")
  void updateChannel_public_with_invalidParameter_failed() throws Exception {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    UpdateChannelRequestDTO updateChannelRequestDTO = new UpdateChannelRequestDTO(
        "n", "n");

    UUID channelId = UUID.randomUUID();
    UpdateChannelResult updateChannelResult = new UpdateChannelResult(
        channelId, ChannelType.PUBLIC, updateChannelRequestDTO.newName(),
        updateChannelRequestDTO.newDescription(), null, Instant.now());

    given(channelService.update(
        channelId, channelMapper.toUpdateChannelCommand(updateChannelRequestDTO)))
        .willReturn(updateChannelResult);

    String requestBody = objectMapper.writeValueAsString(updateChannelRequestDTO);

    // when + then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(status().isBadRequest());
  }
}
