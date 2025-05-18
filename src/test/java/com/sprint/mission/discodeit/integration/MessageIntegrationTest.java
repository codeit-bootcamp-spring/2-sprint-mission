package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("POST /api/messages - 메시지 생성")
    void createMessage_success() throws Exception {
        // given
        User user = new User("user", "pw", "user@example.com", null);
        Channel channel = new Channel("channel", "채널 설명", ChannelType.PUBLIC);
        userRepository.save(user);
        channelRepository.save(channel);

        CreateMessageRequest request = new CreateMessageRequest(user.getId(), channel.getId(),
            "메시지 내용", List.of());

        MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request)
        );

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("메시지 내용"))
            .andExpect(jsonPath("$.channelId").value(channel.getId().toString()))
            .andExpect(jsonPath("$.author.id").value(user.getId().toString()));
    }

    @Test
    @DisplayName("PATCH /api/messages/{id} - 메시지 수정")
    void updateMessage_success() throws Exception {
        // given
        User user = new User("user", "pw", "user@email.com", null);
        Channel channel = new Channel("channel", "설명", ChannelType.PUBLIC);
        userRepository.save(user);
        channelRepository.save(channel);

        Message message = new Message(user, channel, "기존 메시지", List.of());
        Message savedMessage = messageRepository.save(message);

        UpdateMessageRequest request = new UpdateMessageRequest("수정된 메시지 내용");

        // when & then
        mockMvc.perform(patch("/api/messages/" + savedMessage.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("수정된 메시지 내용"));
    }

    @Test
    @DisplayName("DELETE /api/messages/{id} - 메시지 삭제")
    void deleteMessage_success() throws Exception {
        // given
        User user = new User("user", "pw", "user@email.com", null);
        Channel channel = new Channel("channel", "설명", ChannelType.PUBLIC);
        userRepository.save(user);
        channelRepository.save(channel);

        Message message = new Message(user, channel, "삭제할 메시지", List.of());
        Message savedMessage = messageRepository.save(message);

        // when & then
        mockMvc.perform(delete("/api/messages/" + savedMessage.getId()))
            .andExpect(status().isNoContent());

        assertThat(messageRepository.findById(savedMessage.getId())).isEmpty();
    }

    @Test
    @DisplayName("GET /api/messages?channelId={id} - 메시지 목록 조회")
    void getMessagesByChannel_success() throws Exception {
        // given
        User user = new User("user", "pw", "user@email.com", null);
        Channel channel = new Channel("channel", "설명", ChannelType.PUBLIC);
        userRepository.save(user);
        channelRepository.save(channel);

        Message m1 = new Message(user, channel, "메시지1", List.of());
        Message m2 = new Message(user, channel, "메시지2", List.of());
        messageRepository.saveAll(List.of(m1, m2));

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channel.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].content").exists())
            .andExpect(jsonPath("$.content[1].content").exists());
    }

}
