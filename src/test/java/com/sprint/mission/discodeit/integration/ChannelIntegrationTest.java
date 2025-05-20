package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
@Transactional
@ActiveProfiles("test")
class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Update Channel_성공")
  void update_success() throws Exception {
    // given
    Channel channel = Channel.create(ChannelType.PUBLIC, "수정 전 채널", "수정 전 채널입니다.");
    channelRepository.save(channel);

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
        "수정된 채널 이름", "수정 테스트입니다."
    );

    // when, then
    mockMvc.perform(patch("/api/channels/{channelId}", channel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(channel.getId().toString()))
        .andExpect(jsonPath("$.name").value("수정된 채널 이름"))
        .andExpect(jsonPath("$.description").value("수정 테스트입니다."));

    // DB 반영 확인
    Channel updated = channelRepository.findById(channel.getId()).orElseThrow();
    assertThat(updated.getName()).isEqualTo("수정된 채널 이름");
    assertThat(updated.getDescription()).isEqualTo("수정 테스트입니다.");
  }

  @Test
  @DisplayName("Update Channel_실패_id가 유효하지 않을 때")
  void update_fail_whenInvalidId() throws Exception {
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
}
