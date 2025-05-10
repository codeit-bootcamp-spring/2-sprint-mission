package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
public class MessageRepositoryTest {

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private JpaChannelRepository channelRepository;

  @Autowired
  private JpaMessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  private User user;
  private Channel channel;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    user = User.create("a", "a", "test", null);
    user.setUserStatus(UserStatus.create(user, Instant.now()));

    channel = Channel.create("public", "public", ChannelType.PUBLIC);

    user = userRepository.save(user);
    channel = channelRepository.save(channel);

    pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "createdAt"));
  }

  @Test
  void findAllByChannelIdWithAuthor() {
    // given

    List<Message> messages = List.of(
        Message.create(user, channel, "msg1", null),
        Message.create(user, channel, "msg2", null),
        Message.create(user, channel, "msg3", null)
    );

    messageRepository.saveAll(messages);
    em.flush();
    em.clear();
    Instant baseTime = Instant.now();
    // when
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), baseTime, pageable);
    // then
    assertThat(result).hasSize(3);
    assertThat(result.getContent()).extracting(Message::getContent)
        .containsExactly("msg1", "msg2", "msg3");
  }


  @Test
  void findAllByChannel_Id_whenChannelNotExist_thenEmptyResult() {
    // given
    UUID mockId = mock(UUID.class);
    // when
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(mockId, Instant.now(),
        pageable);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void findLastMessageAtByChannelId_Success() {
    // given
    Message msg1 = Message.create(user, channel, "msg1", null);
    Message msg2 = Message.create(user, channel, "msg2", null);
    Message msg3 = Message.create(user, channel, "msg3", null);
    messageRepository.saveAll(List.of(msg1, msg2, msg3));
    em.flush();
    em.clear();
    // when
    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(
        channel.getId());
    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get()).isCloseTo(msg3.getCreatedAt(), within(1, ChronoUnit.MICROS));
  }

}
