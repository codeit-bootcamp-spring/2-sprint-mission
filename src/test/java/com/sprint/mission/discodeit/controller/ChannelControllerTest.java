package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("GET /api/channels?userId=... - 유저의 채널 목록 조회 성공")
    void getChannelsByUserId_success() throws Exception {
        UUID userId = UUID.randomUUID();
        ChannelDto channel = new ChannelDto(
                UUID.randomUUID(),
                "Test Channel",
                "Description",
                ChannelType.PUBLIC,
                Instant.now(),
                List.of(new UserDto(userId, "user", "user@email.com", null, true))
        );

        given(channelService.findAllByUserId(userId)).willReturn(List.of(channel));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/channels")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Channel"))
                .andExpect(jsonPath("$[0].participants[0].username").value("user"));
    }

    @Test
    @DisplayName("GET /api/channels - 유저 ID 누락 시 400 오류")
    void getChannelsByUserId_fail_missingParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/channels"))
                .andExpect(status().isBadRequest());
    }
}