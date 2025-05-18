package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ChannelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("POST /api/channels/public - 공개 채널 생성")
    void createPublicChannel_success() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
            "공개 채널", "채널 설명");

        // when & then
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("공개 채널"))
            .andExpect(jsonPath("$.description").value("채널 설명"));
    }

    @Test
    @DisplayName("POST /api/channels/private - 비공개 채널 생성")
    void createPrivateChannel_success() throws Exception {
        // given
        User user1 = new User("user1", "pw1", "user1@example.com", null);
        User user2 = new User("user2", "pw2", "user2@example.com", null);
        userRepository.saveAll(List.of(user1, user2));

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            List.of(user1.getId(), user2.getId()));

        // when & then
        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type").value("PRIVATE"));
    }

    @Test
    @DisplayName("PATCH /api/channels/{id} - 채널 수정")
    void updateChannel_success() throws Exception {
        // given - 기존 채널 저장
        Channel channel = new Channel(
            "channel",
            "설명",
            ChannelType.PUBLIC
        );
        Channel savedChannel = channelRepository.save(channel);

        UpdateChannelRequest request = new UpdateChannelRequest(
            "newChannel",
            "새 설명"
        );

        // when & then
        mockMvc.perform(patch("/api/channels/" + savedChannel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("newChannel"))
            .andExpect(jsonPath("$.description").value("새 설명"));
    }

    @Test
    @DisplayName("DELETE /api/channels/{id} - 채널 삭제")
    void deleteChannel_success() throws Exception {
        // given
        Channel channel = new Channel(
            "channel",
            "설명",
            ChannelType.PUBLIC
        );
        Channel savedChannel = channelRepository.save(channel);

        // when & then
        mockMvc.perform(delete("/api/channels/" + savedChannel.getId()))
            .andExpect(status().isNoContent());

        assertThat(channelRepository.findById(savedChannel.getId())).isEmpty();
    }

    @Test
    @DisplayName("GET /api/channels?userId={id} - 참여 채널 목록 조회")
    void findAllChannelsByUser_success() throws Exception {
        // given
        User user = new User("user", "pw123", "test@email.com", null);
        User savedUser = userRepository.save(user);

        Channel ch1 = new Channel("채널1", "설명1", ChannelType.PUBLIC);
        Channel ch2 = new Channel("채널2", "설명2", ChannelType.PUBLIC);
        channelRepository.saveAll(List.of(ch1, ch2));

        // when & then
        mockMvc.perform(get("/api/channels")
                .param("userId", savedUser.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

}
