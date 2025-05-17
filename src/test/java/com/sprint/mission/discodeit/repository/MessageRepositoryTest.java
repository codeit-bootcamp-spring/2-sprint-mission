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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  private User user;
  private Channel channel;

  @BeforeEach
  void setup() {
    BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
    user = new User("testUser", "test@example.com", "password123!@#", profile);
    userRepository.save(user);
    new UserStatus(user, Instant.now());

    channel = new Channel(ChannelType.PUBLIC, "Test Channel", "테스트용 채널");
    channelRepository.save(channel);

    entityManager.flush();
    entityManager.clear();
  }

  private Message createTestMessage(String content, Channel channel, User author,
      Instant createdAt) {
    Message message = new Message(content, channel, author, new ArrayList<>());

    if (createdAt != null) {
      ReflectionTestUtils.setField(message, "createdAt", createdAt);
    }

    Message saved = messageRepository.save(message);
    entityManager.flush();
    return saved;
  }

  @Test
  @DisplayName("페이징 및 정렬된 메시지 목록 조회")
  void testFindMessagesPagedAndSorted_Success() {
    Instant now = Instant.now();

    createTestMessage("메시지1", channel, user, now.minus(10, ChronoUnit.MINUTES));
    createTestMessage("메시지2", channel, user, now.minus(5, ChronoUnit.MINUTES));
    createTestMessage("메시지3", channel, user, now.minus(1, ChronoUnit.MINUTES));

    entityManager.clear();

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        now.plus(1, ChronoUnit.MINUTES),
        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    assertThat(slice).isNotNull();
    assertThat(slice.hasContent()).isTrue();
    assertThat(slice.getNumberOfElements()).isEqualTo(2);
    assertThat(slice.hasNext()).isTrue();

    List<Message> content = slice.getContent();
    assertThat(content.get(0).getCreatedAt()).isAfterOrEqualTo(content.get(1).getCreatedAt());
    assertThat(Hibernate.isInitialized(content.get(0).getAuthor())).isTrue();
  }

  @Test
  @DisplayName("메시지가 없는 채널일 경우 페이징 및 정렬된 메시지 목록 조회 시 빈 값 반환")
  void testFindMessagesPagedAndSorted_Empty() {
    Instant now = Instant.now();

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        now.plus(1, ChronoUnit.MINUTES),
        PageRequest.of(0, 10)
    );

    assertThat(slice).isNotNull();
    assertThat(slice.hasContent()).isFalse();
    assertThat(slice.getContent()).isEmpty();
  }

  @Test
  @DisplayName("채널의 마지막 메시지 생성시간 조회")
  void testFindLastMessageTime_Success() {
    Instant now = Instant.now();

    createTestMessage("msg1", channel, user, now.minus(10, ChronoUnit.MINUTES));
    createTestMessage("msg2", channel, user, now.minus(5, ChronoUnit.MINUTES));
    Message lastMessage = createTestMessage("msg3", channel, user, now);

    entityManager.clear();

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    assertThat(lastMessageAt).isPresent();
    assertThat(lastMessageAt.get().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(lastMessage.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  @DisplayName("메시지가 없는 채널일 경우 마지막 메시지 시간 조회 시 빈 값 반환")
  void testFindLastMessageTime_Empty() {
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    assertThat(lastMessageAt).isEmpty();
  }
}
