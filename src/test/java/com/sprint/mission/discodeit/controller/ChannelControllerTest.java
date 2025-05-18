package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  private static final String BASE_URL = "/api/channels";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private JwtUtil jwtUtil;

  @DisplayName("POST /private - private 채널 생성 성공")
  @Test
  void createPrivateChannel_Success() throws Exception {
    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(
        List.of()
    );
    ChannelDto responseDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PRIVATE,
        "",
        "",
        List.of(),
        Instant.now()
    );

    given(channelService.createPrivateChannel(any(CreatePrivateChannelRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(post(BASE_URL + "/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDto.id().toString()));
  }

  @DisplayName("POST /private - 유효성 검사 실패")
  @Test
  void createPrivateChannel_InvalidRequest_Fail() throws Exception {
    CreatePrivateChannelRequest invalidRequest = new CreatePrivateChannelRequest(null);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("POST /public - public 채널 생성 성공")
  @Test
  void createPublicChannel_Success() throws Exception {
    CreatePublicChannelRequest request = new CreatePublicChannelRequest(
        "test",
        "test"
    );
    ChannelDto responseDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "test",
        "test",
        List.of(),
        Instant.now()
    );

    given(channelService.createPublicChannel(any(CreatePublicChannelRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(post(BASE_URL + "/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
        .andExpect(jsonPath("$.name").value(responseDto.name()));


  }

  @DisplayName("PATCH /{channelId} - 채널 수정 성공")
  @Test
  void updateChannel_Success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UpdateChannelRequest request = new UpdateChannelRequest(
        "new",
        "newdis"
    );
    ChannelDto responseDto = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "new",
        "newids",
        List.of(),
        Instant.now()
    );

    given(channelService.updateChannel(eq(channelId), any(UpdateChannelRequest.class)))
        .willReturn(responseDto);

    mockMvc.perform(patch(BASE_URL + "/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value(responseDto.name()));
  }

  @DisplayName("DELETE /{channelId} - 채널 삭제 성공")
  @Test
  void deleteChannel_Success() throws Exception {
    UUID channelId = UUID.randomUUID();

    mockMvc.perform(delete(BASE_URL + "/" + channelId))
        .andExpect(status().isNoContent());
  }

}

