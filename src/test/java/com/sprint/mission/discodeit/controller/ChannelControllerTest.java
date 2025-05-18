package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
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
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicChannelService channelService;

  @Test
  @DisplayName("Update Channel_성공")
  void update_shouldReturnUpdatedChannel() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();

    PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
        "수정된 채널 이름", "수정 테스트입니다.");

    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "수정된 채널 이름", "수정 테스트입니다.",
        List.of(), null);

    // when
    when(channelService.updateChannel(channelId, publicChannelUpdateRequest)).thenReturn(
        channelDto);

    // then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(publicChannelUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(channelDto.id().toString()))
        .andExpect(jsonPath("$.name").value("수정된 채널 이름"))
        .andExpect(jsonPath("$.description").value("수정 테스트입니다."));
  }

  @Test
  @DisplayName("Update Channel_실패_id가 유효하지 않을 때")
  void update_shouldReturnChannelNotFound_whenChannelIdInvalid() throws Exception {
    // given
    UUID invalidChannelId = UUID.randomUUID();

    PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
        "수정된 채널 이름", "수정 테스트입니다.");

    ChannelDto channelDto = new ChannelDto(invalidChannelId, ChannelType.PUBLIC, "수정된 채널 이름",
        "수정 테스트입니다.",
        List.of(), null);

    // when
    when(channelService.updateChannel(eq(invalidChannelId), any()))
        .thenThrow(new ChannelNotFoundException(invalidChannelId));

    // then
    mockMvc.perform(patch("/api/channels/{channelId}", invalidChannelId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(publicChannelUpdateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
  }


}
