package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  UserRepository userRepository;

  @Test
  void findAllByChannelIdWithAuthor_페이징_성공() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "general", ""));
    User user = userRepository.save(new User("tester", "t@email.com", "pw", null));
    Message message = new Message("hello", channel, user, List.of());
    messageRepository.save(message);

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), Instant.now().plusSeconds(1), PageRequest.of(0, 10));

    assertEquals(1, result.getContent().size());
  }

  @Test
  void findLastMessageAtByChannelId_성공() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "g", ""));
    User user = userRepository.save(new User("a", "a@a.com", "1", null));
    Message msg = messageRepository.save(new Message("hi", channel, user, List.of()));

    Optional<Instant> latest = messageRepository.findLastMessageAtByChannelId(channel.getId());

    assertTrue(latest.isPresent());
  }
}
