package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private TestEntityManager em;

  @Test
  public void findAllByChannelIdWithAuthor_shouldReturnMessages() {
    Instant base = Instant.now();

    User user1 = createUser("user1", "user1@gmail.com");
    Channel channel1 = createChannel(ChannelType.PUBLIC, "channel1", "channel1");
    Message message1 = createMessage("message1", channel1, user1, base);

    User user2 = createUser("user2", "user2@gmail.com");
    Message message2 = createMessage("message2", channel1, user2,
        base.minus(3, ChronoUnit.MINUTES));
    Message message3 = createMessage("message3", channel1, user2,
        base.minus(5, ChronoUnit.MINUTES));
    em.flush();
    em.clear();

    Instant cursor = Instant.now().plusSeconds(1);
    Pageable pageable = PageRequest.of(
        0, 2,
        Sort.by(Direction.DESC, "createdAt")
    );

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        channel1.getId(), cursor, pageable
    );

    List<Message> messages = slice.getContent();
    Message message = messages.get(0);

    assertThat(slice.getContent()).hasSize(2);
    assertThat(slice.hasNext()).isTrue();
    assertThat(messages.get(0).getCreatedAt()).isAfterOrEqualTo(messages.get(1).getCreatedAt());
    assertThat(message.getAuthor()).isNotNull();
    assertThat(message.getAuthor().getProfile()).isNotNull();
  }

  @Test
  public void findByIdWithAuthorAndAttachments_shouldFetchJoinSuccessfully() {
    User user = createUser("user1", "user1@gmail.com");
    Channel channel = createChannel(ChannelType.PUBLIC, "channel1", "channel1");
    Message message = createMessage("message1", channel, user, Instant.now());
    em.flush();
    em.clear();

    Optional<Message> res = messageRepository.findByIdWithAuthorAndAttachments(message.getId());

    assertThat(res).isPresent();
    assertThat(res.get().getAuthor()).isNotNull();
    assertThat(res.get().getAuthor().getProfile()).isNotNull();
    assertThat(res.get().getAuthor().getProfile().getFileName()).isEqualTo("human.jpg");
    assertThat(res.get().getAttachments()).isNotNull();
  }

  @Test
  public void findLastMessageTimeByChannelId_shouldReturnLastMessageTime() {
    Instant base = Instant.now();
    User user = createUser("user1", "user1@gmail.com");
    Channel channel = createChannel(ChannelType.PUBLIC, "channel1", "channel1");
    Message message1 = createMessage("message1", channel, user, base);
    Message message2 = createMessage("message2", channel, user, base.minus(5, ChronoUnit.MINUTES));
    em.flush();
    em.clear();

    Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId());

    assertThat(lastMessageTime).isAfterOrEqualTo(message2.getCreatedAt());
  }

  private User createUser(String username, String email) {
    BinaryContent profile = new BinaryContent("human.jpg", "image/jpeg", 1024L);
    User user = new User(username, email, "user1234", profile);
    UserStatus status = new UserStatus(user, Instant.now());

    em.persist(profile);
    em.persist(user);
    em.persist(status);

    return user;
  }

  private Channel createChannel(ChannelType type, String name, String description) {
    Channel channel = new Channel(type, name, description);
    em.persist(channel);
    return channel;
  }

  private Message createMessage(String content, Channel channel, User user, Instant createdAt) {
    BinaryContent attachment = new BinaryContent("human2.jpg", "image/jpeg", 1024L);
    Message message = new Message(content, channel, user, List.of(attachment));
    ReflectionTestUtils.setField(message, "createdAt", createdAt);
    em.persist(message);

    return message;
  }
}
