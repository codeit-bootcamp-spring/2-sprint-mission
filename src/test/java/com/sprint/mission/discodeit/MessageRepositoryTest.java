package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("특정 채널의 마지막 메시지 시간 조회 - 성공")
  void findLastMessageAtByChannelId_success() {
    User user = new User("msgUser", "msg@example.com", "pass", null);
    userRepository.save(user);
    Channel channel = new Channel(ChannelType.PUBLIC, "msgChannel", "desc");
    channelRepository.save(channel);
    Message message = new Message("Hello", channel, user, List.of());
    messageRepository.save(message);

    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("특정 채널의 마지막 메시지 시간 조회 - 실패")
  void findLastMessageAtByChannelId_failure() {
    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(UUID.randomUUID());
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("채널 ID와 시간으로 메시지 조회 - 실패")
  void findAllByChannelIdWithAuthor_failure() {
    PageRequest pageable = PageRequest.of(0, 10);
    var result = messageRepository.findAllByChannelIdWithAuthor(UUID.randomUUID(), Instant.now(),
        pageable);
    assertThat(result).isEmpty();
  }
}
