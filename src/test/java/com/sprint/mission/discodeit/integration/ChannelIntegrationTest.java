package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.integration.base.BaseIntegrationTest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChannelIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ReadStatusRepository readStatusRepository;

    @Autowired
    UserRepository userRepository;

    private User setUser1;
    private User setUser2;
    private Channel setPublicChannel;
    private Channel setPrivateChannel;
    private ReadStatus setReadStatus1;
    private ReadStatus setReadStatus2;

    @BeforeEach
    void setup() {
        readStatusRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        setUser1 = userRepository.save(new User("user1", "abc123@@", "user1@test.com", null));
        setUser2 = userRepository.save(new User("user2", "abc123@@", "user2@test.com", null));
        setPublicChannel = channelRepository.save(
            new Channel("setPublic", "setPublicTest", ChannelType.PUBLIC));
        setPrivateChannel = channelRepository.save(new Channel(null, null, ChannelType.PRIVATE));
        setReadStatus1 = readStatusRepository.save(
            new ReadStatus(setUser1, setPrivateChannel, Instant.now()));
        setReadStatus2 = readStatusRepository.save(
            new ReadStatus(setUser2, setPrivateChannel, Instant.now()));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 공개_채널_생성_성공() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("public", "test");

        mockMvc.perform(post("/api/channels/public")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("public"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.name()))
            .andDo(print());
    }

    @Test
    void 공개_채널_생성_채널_이름_공백_실패() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("", "test");

        mockMvc.perform(post("/api/channels/public")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 비공개_채널_생성_성공() throws Exception {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            List.of(setUser1.getId(), setUser2.getId()));

        mockMvc.perform(post("/api/channels/private")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value(ChannelType.PRIVATE.name()))
            .andDo(print());
    }

    @Test
    void 비공개_채널_생성_사용자_조회_실패() throws Exception {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            List.of(setUser1.getId(), UUID.randomUUID()));

        mockMvc.perform(post("/api/channels/private")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 공개_채널_수정_성공() throws Exception {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("updatePublic", "test");

        mockMvc.perform(patch("/api/channels/{channelId}", setPublicChannel.getId())
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("updatePublic"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.type").value(ChannelType.PUBLIC.name()))
            .andDo(print());
    }

    @Test
    void 비공개_채널_수정_실패() throws Exception {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("updatePublic", "test");

        mockMvc.perform(patch("/api/channels/{channelId}", setPrivateChannel.getId())
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 채널_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/channels/{channelId}", setPublicChannel.getId()))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 채널_삭제_채널_아이디_조회_실패() throws Exception {
        mockMvc.perform(delete("/api/channels/{channelId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}
