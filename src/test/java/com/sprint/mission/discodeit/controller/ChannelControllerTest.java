package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    //json-> 객체로 변환
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    private PublicChannelCreateRequest publicChannelCreateRequest;
    private PrivateChannelCreateRequest privateChannelCreateRequest;
    private PublicChannelUpdateRequest publicChannelUpdateRequest;
    private ChannelDto publicChannelDto;
    private ChannelDto privateChannelDto;
    private UUID channelId;
    private UUID userId;
    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();

        userDto1 = new UserDto(userId, "testUser", "test@mail.com", null, true);
        Set<UserDto> participants = new HashSet<>(Collections.singletonList(userDto1));

        publicChannelCreateRequest = new PublicChannelCreateRequest("Public Test Channel",
            "Test Description");
        privateChannelCreateRequest = new PrivateChannelCreateRequest(
            Collections.singletonList(userId), "Private Chat Name");
        publicChannelUpdateRequest = new PublicChannelUpdateRequest("Updated Public Name",
            "Updated Desc");

        publicChannelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "Public Test Channel",
            "Test Description", Collections.emptySet(), Instant.now());
        privateChannelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE,
            "Private Chat Name", null, participants, Instant.now());
    }

    @Test
    @DisplayName("공개 채널 생성 - 성공")
    void createPublicChannel_Success() throws Exception {

        given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(
            publicChannelDto);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publicChannelCreateRequest)))

            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(publicChannelDto.name()))
            .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.toString()));
    }

    @Test
    @DisplayName("비공개 채널 생성 - 성공")
    void createPrivateChannel_Success() throws Exception {
        given(channelService.create(any(PrivateChannelCreateRequest.class))).willReturn(
            privateChannelDto);

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(privateChannelCreateRequest)))

            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(
                privateChannelDto.name()))
            .andExpect(jsonPath("$.type").value(ChannelType.PRIVATE.toString()))
            .andExpect(jsonPath("$.participants", hasSize(1)));
    }

    @Test
    @DisplayName("공개 채널 업데이트 - 성공")
    void updatePublicChannel_Success() throws Exception {
        // given
        ChannelDto updatedDto = new ChannelDto(channelId, ChannelType.PUBLIC,
            publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription(),
            Collections.emptySet(), Instant.now());
        given(
            channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class))).willReturn(
            updatedDto);

        // when & then
        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publicChannelUpdateRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(updatedDto.name()))
            .andExpect(jsonPath("$.description").value(updatedDto.description()));
    }

    @Test
    @DisplayName("채널 삭제 - 성공")
    void deleteChannel_Success() throws Exception {
        // given
        doNothing().when(channelService).delete(channelId);

        // when & then
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("채널 삭제 - 실패 (채널 없음)")
    void deleteChannel_Failure_ChannelNotFound() throws Exception {
        // given
        doThrow(
            new ChannelException(com.sprint.mission.discodeit.exception.ErrorCode.CHANNEL_NOT_FOUND,
                Collections.emptyMap()))
            .when(channelService).delete(channelId);

        // when & then
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
            .andExpect(status().isNotFound()); // Assuming GlobalExceptionHandler maps this to 404
    }

    @Test
    @DisplayName("사용자 ID로 채널 목록 조회 - 성공")
    void findAllChannelsByUserId_Success() throws Exception {
        // given
        List<ChannelDto> channels = Arrays.asList(publicChannelDto, privateChannelDto);
        given(channelService.findAllByUserId(userId)).willReturn(channels);

        // when & then
        mockMvc.perform(get("/api/channels")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value(publicChannelDto.name()))
            .andExpect(jsonPath("$[1].name").value(privateChannelDto.name()));
    }

    @Test
    @DisplayName("사용자 ID로 채널 목록 조회 - 빈 목록")
    void findAllChannelsByUserId_Empty() throws Exception {
        // given
        given(channelService.findAllByUserId(userId)).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/channels")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    // Note: FindById (GET /api/channels/{channelId}) endpoint is not in ChannelController.
} 