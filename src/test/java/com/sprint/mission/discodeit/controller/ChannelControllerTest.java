package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFound;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    // 1. Public 채널 생성 성공
    @Test
    @DisplayName("Public 채널 생성 성공 - 201 Created")
    void createPublicChannel_success() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("General", "일반 채널");
        ChannelDto response = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "PUBLIC", "일반 채널", Collections.emptyList(), Instant.now());

        given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("일반 채널"));
    }

    // 2. Private 채널 생성 성공
    @Test
    @DisplayName("Private 채널 생성 성공 - 201 Created")
    void createPrivateChannel_success() throws Exception {
        // given
        List<UUID> participants = List.of(UUID.randomUUID());
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participants);
        ChannelDto response = new ChannelDto(UUID.randomUUID(),ChannelType.PRIVATE, "PRIVATE", "일반 채널", Collections.emptyList(), Instant.now());

        given(channelService.create(any(PrivateChannelCreateRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/channels/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("PRIVATE"));
    }

    // 3. 채널 수정 성공
    @Test
    @DisplayName("채널 수정 성공 - 200 OK")
    void updateChannel_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Updated General", "수정된 설명");
        ChannelDto response = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "Updated General", "수정된 설명", Collections.emptyList(), Instant.now());

        given(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("수정된 설명"));
    }

    // 4. 채널 삭제 성공
    @Test
    @DisplayName("채널 삭제 성공 - 204 No Content")
    void deleteChannel_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        willDoNothing().given(channelService).delete(channelId);

        // when & then
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                .andExpect(status().isNoContent());
    }

    // 5. 채널 수정 실패 - 존재하지 않는 채널
    @Test
    @DisplayName("채널 수정 실패 - 존재하지 않는 채널 (404 Not Found)")
    void updateChannel_fail_channelNotFound() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Updated", "수정 시도");

        given(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class)))
                .willThrow(new ChannelNotFound());

        // when & then
        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // 6. Public 채널 생성 실패 - 유효성 검증 실패
    @Test
    @DisplayName("Public 채널 생성 실패 - 유효성 검증 (400 Bad Request)")
    void createPublicChannel_fail_validation() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, "설명");

        // when & then
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}