package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService channelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublicChannel_success() throws Exception {
        PublicChannelCreateRequest req = new PublicChannelCreateRequest("테스트채널", "설명");
        ChannelDto res = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "테스트채널", "설명", null, Instant.now());

        Mockito.when(channelService.create(any(PublicChannelCreateRequest.class)))
                .thenReturn(res);

        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("테스트채널"));
    }

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void createPrivateChannel_success() throws Exception {
        PrivateChannelCreateRequest req = new PrivateChannelCreateRequest(List.of(UUID.randomUUID()));
        ChannelDto res = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "비밀채널", "Private channel", null, Instant.now());

        Mockito.when(channelService.create(any(PrivateChannelCreateRequest.class)))
                .thenReturn(res);

        mockMvc.perform(post("/api/channels/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("비밀채널"));
    }

    @Test
    @DisplayName("채널 수정 성공")
    void updateChannel_success() throws Exception {
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest req = new PublicChannelUpdateRequest("수정채널", "UPDATED CHANNEL");
        ChannelDto res = new ChannelDto(channelId, ChannelType.PUBLIC, "수정채널", "UPDATED CHANNEL", null, Instant.now());

        Mockito.when(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class)))
                .thenReturn(res);

        mockMvc.perform(patch("/api/channels/" + channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정채널"));
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteChannel_success() throws Exception {
        UUID channelId = UUID.randomUUID();

        Mockito.doNothing().when(channelService).delete(channelId);

        mockMvc.perform(delete("/api/channels/" + channelId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("유저의 채널 목록 조회 성공")
    void findAllChannels_success() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        ChannelDto ch1 = new ChannelDto(channelId, ChannelType.PUBLIC, "채널A", "channel A", null, Instant.now());
        ChannelDto ch2 = new ChannelDto(channelId, ChannelType.PUBLIC, "채널B", "channel B", null, Instant.now());

        Mockito.when(channelService.findAllByUserId(userId))
                .thenReturn(List.of(ch1, ch2));

        mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("채널A"));
    }

    @Test
    @DisplayName("공개 채널 생성 실패 - 이름 누락")
    void createPublicChannel_fail_blankName() throws Exception {
        PublicChannelCreateRequest req = new PublicChannelCreateRequest("", "설명");

        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
