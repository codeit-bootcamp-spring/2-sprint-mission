package com.sprint.mission.discodeit.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.handler.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
public class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChannelService channelService;

    @Test
    @DisplayName("POST /api/channels/public - 공개 채널 생성 성공")
    void createPublicChannel_success() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("일반채널", "일반채널입니다.");
        UUID channelId = UUID.randomUUID();
        ChannelDto response = new ChannelDto(channelId, ChannelType.PUBLIC, "일반채널",
            "일반채널입니다.", Collections.emptyList(), Instant.now());

        given(channelService.createPublicChannel(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(channelId.toString()))
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andExpect(jsonPath("$.name").value("일반채널"))
            .andExpect(jsonPath("$.description").value("일반채널입니다."));
    }

    @Test
    @DisplayName("PATCH /api/channels/{channelId} - 채널 수정 성공")
    void updateChannel_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UpdateChannelRequest request = new UpdateChannelRequest("변경된이름", "변경된설명");
        ChannelDto response = new ChannelDto(channelId, ChannelType.PUBLIC, "변경된이름",
            "변경된설명", Collections.emptyList(), Instant.now());

        given(channelService.updateChannel(any(), any())).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(channelId.toString()))
            .andExpect(jsonPath("$.name").value("변경된이름"))
            .andExpect(jsonPath("$.description").value("변경된설명"));
    }

    @Test
    @DisplayName("PATCH /api/channels/{channelId} - 존재하지 않는 채널 수정 시 예외")
    void updateChannel_notFound_failure() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UpdateChannelRequest request = new UpdateChannelRequest("이름", "설명");

        given(channelService.updateChannel(any(), any()))
            .willThrow(new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

        // when & then
        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.CHANNEL_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("DELETE /api/channels/{channelId} - 채널 삭제 성공")
    void deleteChannel_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
            .andExpect(status().isNoContent());
    }
}
