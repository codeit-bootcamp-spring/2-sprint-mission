package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@EntityScan(basePackages = "com.sprint.mission.discodeit.entity")
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ChannelRepository channelRepository;

  private User createUser(String username) {
    return userRepository.save(new User(username, username + "@test.com", "password", null, null));
  }

  private Channel createChannel(String name) {
    return channelRepository.save(new Channel(name, "test", ChannelType.PUBLIC));
  }

  private Message createMessage(User user, Channel channel, String content, Instant createdAt) {
    Message message = new Message(
        content,
        channel,
        user,
        null
    );
    ReflectionTestUtils.setField(message, "createdAt", createdAt);
    return messageRepository.save(message);
  }

  @Test
  @DisplayName("채널 ID로 최신 메시지 조회 - 성공")
  void findTopByChannelIdOrderByCreatedAtDescSuccess() {
    // given
    User user = createUser("test");
    Channel channel = createChannel("test");

    createMessage(user, channel, "old", Instant.parse("2024-01-01T00:00:00Z"));
    createMessage(user, channel, "new", Instant.now());

    // when
    Optional<Message> result = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
        channel.getId());

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getContent()).isEqualTo("new");
  }

  @Test
  @DisplayName("채널+작성자 기준 메시지 목록 조회")
  void findAllByChannelIdAndAuthorId() {
    // given
    User user = createUser("test");
    Channel channel = createChannel("test");

    createMessage(user, channel, "msg1", Instant.now());

    // when
    List<Message> messages = messageRepository.findAllByChannelIdAndAuthorId(channel.getId(),
        user.getId());

    // then
    assertThat(messages).hasSize(1);
  }

  @Test
  @DisplayName("작성자 기준 전체 메시지 조회")
  void findAllByAuthorIdSuccess() {
    //given
    User user = createUser("test");
    Channel channel = createChannel("test");

    createMessage(user, channel, "test", Instant.now());

    // when
    List<Message> list = messageRepository.findAllByAuthorId(user.getId());

    // then
    assertThat(list).isNotEmpty();
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 (EntityGraph 포함)")
  void findAllByChannelIdWithEntityGraph() {
    // given
    User user = createUser("test");
    Channel channel = createChannel("test");

    createMessage(user, channel, "test", Instant.now());

    // when
    List<Message> messages = messageRepository.findAllByChannelId(channel.getId());

    // then
    assertThat(messages).isNotEmpty();
    assertThat(messages.get(0).getAuthor().getUsername()).isEqualTo("test");
  }

  @Test
  @DisplayName("채널 ID로 페이징 메시지 조회")
  void findByChannelIdWithPaging() {
    // given
    User user = createUser("test");
    Channel channel = createChannel("test");

    for (int i = 0; i < 5; i++) {
      createMessage(user, channel, "msg" + i, Instant.now().minusSeconds(i));
    }

    // when
    Slice<Message> page = messageRepository.findByChannelId(channel.getId(), PageRequest.of(0, 3));

    // then
    assertThat(page.getContent()).hasSize(3);
  }

  @Test
  @DisplayName("Cursor 기반 이전 메시지 페이징 조회")
  void findByChannelIdAndCreatedAtLessThan() {
    // given
    User user = createUser("cursorUser");
    Channel channel = createChannel("cursorChannel");

    Instant now = Instant.now();
    createMessage(user, channel, "msg1", now.minusSeconds(10));
    createMessage(user, channel, "msg2", now.minusSeconds(5));
    createMessage(user, channel, "msg3", now);

    // when
    Slice<Message> result = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
        channel.getId(), now, PageRequest.of(0, 2));

    // then
    assertThat(result.getContent()).hasSize(2);
  }
}
