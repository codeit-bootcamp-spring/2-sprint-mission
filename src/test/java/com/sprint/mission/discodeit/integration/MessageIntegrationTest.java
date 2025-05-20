package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("deleteMessage_성공")
  void deleteMessage_success() throws Exception {
    // given: DB에 메시지 저장
    User author = User.create("유저", "test@email.com", "password123!", null);
    userRepository.save(author);

    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel", "테스트용 Public 채널입니다.");
    channelRepository.save(channel);

    Message message = Message.create(author, channel, "테스트용", List.of());
    messageRepository.save(message);

    UUID messageId = message.getId();

    // when & then: 메시지 삭제 요청 후 204 응답 확인
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // then: DB에서 삭제됐는지 확인
    boolean exists = messageRepository.existsById(messageId);
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("deleteMessage_실패_존재하지 않는 ID")
  void deleteMessage_fail_notFound() throws Exception {
    // given: DB에 없는 UUID 생성
    UUID invalidMessageId = UUID.randomUUID();

    // when & then: 삭제 요청 시 404 응답 및 에러 메시지 검증
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/{messageId}", invalidMessageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("메시지를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
  }

}
