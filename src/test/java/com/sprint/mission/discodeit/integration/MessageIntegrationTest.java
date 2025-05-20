package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.integration.base.BaseIntegrationTest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.TestMultipartUtil;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class MessageIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    private User user;
    private Channel channel;
    private Message message;

    @BeforeEach
    void setup() {
        messageRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(new User("testUser", "pass123@@", "user@test.com", null));
        channel = channelRepository.save(new Channel("public", "test", ChannelType.PUBLIC));
        message = messageRepository.save(new Message("setMessage", user, channel, null));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 메세지_생성_성공() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest(channel.getId(), user.getId(),
            "메세지 본문입니다.");
        MockMultipartFile jsonPart = TestMultipartUtil.jsonPart("messageCreateRequest", request);

        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("메세지 본문입니다."))
            .andExpect(jsonPath("$.author.username").value("testUser"))
            .andDo(print());
    }

    @Test
    void 메세지_생성_실패_존재하지_않는_채널() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest(UUID.randomUUID(), user.getId(),
            "내용");
        MockMultipartFile jsonPart = TestMultipartUtil.jsonPart("messageCreateRequest", request);

        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 메세지_수정_성공() throws Exception {
        MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지입니다.");

        mockMvc.perform(patch("/api/messages/{messageId}", message.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("수정된 메시지입니다."))
            .andDo(print());
    }

    @Test
    void 메세지_수정_메세지_아이디_조회_실패() throws Exception {
        MessageUpdateRequest request = new MessageUpdateRequest("내용");

        mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 메세지_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/messages/{messageId}", message.getId()))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void 메세지_삭제_실패_메세지_아이디_없음() throws Exception {
        mockMvc.perform(delete("/api/messages/{messageId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 메세지_목록_조회_성공() throws Exception {
        mockMvc.perform(get("/api/messages")
                .param("channelId", channel.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].content").value("setMessage"))
            .andDo(print());
    }
}
