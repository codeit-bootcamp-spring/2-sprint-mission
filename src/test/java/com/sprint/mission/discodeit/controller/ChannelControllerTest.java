package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID user1Id;
    private UUID publicChannelId;
    private UUID privateChannelId;
    private List<UUID> participantIds;
    private List<UserDto> participants;

    @BeforeEach
    void setUp() {
        user1Id = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();
        participantIds = Arrays.asList(user1Id, user2Id);
        publicChannelId = UUID.randomUUID();
        privateChannelId = UUID.randomUUID();

        UserDto user1Dto = new UserDto(user1Id, "user1", "test", null, true);
        UserDto user2Dto = new UserDto(user2Id, "user2", "test", null, true);
        participants = Arrays.asList(user1Dto, user2Dto);
    }

    @Test
    void 공개_채널_생성_테스트() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("public", "test");

        ChannelDto channelDto = new ChannelDto(publicChannelId, ChannelType.PUBLIC, request.name(),
            request.description(), null, Instant.now());

        given(channelService.createPublicChannel(request)).willReturn(channelDto);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void 공개_채널_생성_요청_값_없음_테스트() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, null);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 비공개_채널_생성_테스트() throws Exception {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

        ChannelDto channelDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE, null, null,
            participants, Instant.now());

        given(channelService.createPrivateChannel(request)).willReturn(channelDto);

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void 비공개_채널_참여_사용자_값_없음_테스트() throws Exception {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of());

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 채널_수정_테스트() throws Exception {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("updateTest",
            "testing");
        ChannelDto channelDto = new ChannelDto(publicChannelId, ChannelType.PUBLIC,
            request.newName(), request.newDescription(), null, Instant.now());

        given(channelService.updateChannel(publicChannelId, request)).willReturn(channelDto);

        mockMvc.perform(patch("/api/channels/{channelId}", publicChannelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 채널_수정_공백_값_테스트() throws Exception {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("", "");

        mockMvc.perform(patch("/api/channels/{channelId}", publicChannelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 채널_삭제_테스트() throws Exception {
        mockMvc.perform(delete("/api/channels/{channelId}", publicChannelId))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 채널_삭제_아이디_값_없음_테스트() throws Exception {
        doThrow(ChannelNotFoundException.forId(publicChannelId.toString())).when(channelService)
            .deleteChannel(publicChannelId);

        mockMvc.perform(delete("/api/channels/{channelId}", publicChannelId))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 채널_목록_조회() throws Exception {
        ChannelDto publicChannelDto = new ChannelDto(publicChannelId, ChannelType.PUBLIC, "public",
            "test", null, Instant.now());
        ChannelDto privateChannelDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE, null,
            null, participants, Instant.now());
        List<ChannelDto> channelDtos = Arrays.asList(publicChannelDto, privateChannelDto);

        given(channelService.findAllByUserId(user1Id)).willReturn(channelDtos);

        mockMvc.perform(get("/api/channels")
                .param("userId", user1Id.toString()))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 채널_목록_조회_사용자_아이디_없음_테스트() throws Exception {
        doThrow(UserNotFoundException.forId(user1Id.toString())).when(channelService)
            .findAllByUserId(user1Id);

        mockMvc.perform(get("/api/channels")
                .param("userId", user1Id.toString()))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

}