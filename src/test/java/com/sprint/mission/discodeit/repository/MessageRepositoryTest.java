package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Test
  void findAllByChannelIdWithAuthor() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "channel", "desc"));
    User user = userRepository.save(new User("user", "user@test.com", "1234", null));
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    Message message = messageRepository.save(new Message("test", channel, user, List.of()));

    entityManager.flush();

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        message.getCreatedAt().plusSeconds(1),
        PageRequest.of(0, 10));

    assertNotNull(result);
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("test");
    assertThat(result.getContent().get(0).getAuthor().getEmail()).isEqualTo("user@test.com");
    assertThat(result.getContent().get(0).getAuthor().getStatus()).isNotNull();
  }

  @Test
  void findLastMessageAtByChannelId() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "channel", "desc"));
    User user = userRepository.save(new User("user", "user@test.com", "1234", null));
    Message message = messageRepository.save(new Message("test", channel, user, List.of()));

    entityManager.flush();

    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

    assertTrue(result.isPresent());
  }
}