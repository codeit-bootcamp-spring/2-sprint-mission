package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MessageIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MessageRepository messageRepository;
    @Autowired UserRepository userRepository;
    @Autowired ChannelRepository channelRepository;

    private User user;
    private Channel channel;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {
        user = new User("tester", "test@email.com", "pw", null);
        userStatus = new UserStatus(user, Instant.now());

        user = userRepository.save(user);

        channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "테스트 채널", "TEST CHANNEL"));
    }


    @Test
    @DisplayName("메시지 생성 성공")
    void createMessage_success() throws Exception {


        MessageCreateRequest req = new MessageCreateRequest(
                "안녕하세요",
                channel.getId(),
                user.getId()
        );

        MockMultipartFile messagePart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(req)
        );

        mockMvc.perform(multipart("/api/messages")
                        .file(messagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("안녕하세요"))
                .andExpect(jsonPath("$.channelId").value(channel.getId().toString()))
                .andExpect(jsonPath("$.author.id").value(user.getId().toString()));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 채널ID 없음")
    void createMessage_fail_noChannelId() throws Exception {
        MessageCreateRequest req = new MessageCreateRequest(
                "채널 없음",
                null,
                user.getId()
        );

        MockMultipartFile messagePart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(req)
        );

        mockMvc.perform(multipart("/api/messages")
                        .file(messagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.channelId").value("채널을 반드시 입력해야 합니다."));
    }
}
