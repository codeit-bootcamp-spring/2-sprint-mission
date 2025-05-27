package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 사용자 생성
   */
  private User createTestUser(String username, String email) {
    User user = User.create(username, email, "password123!@#", null);
    UserStatus status = UserStatus.create(user, Instant.now());
    return userRepository.save(user);
  }

  /**
   * TestFixture: 채널 생성
   */
  private Channel createTestChannel(ChannelType type, String name, String description) {
    Channel channel = Channel.create(type, name, description);
    return channelRepository.save(channel);
  }

  /**
   * TestFixture: 메시지 생성
   */
  private Message createTestMessage(User author, Channel channel, String content,
      Instant createdAt) {
    Message message = Message.create(author, channel, content, new ArrayList<>());
    if (createdAt != null) {
      ReflectionTestUtils.setField(message, "createdAt", createdAt);
    }
    return messageRepository.save(message);
  }

  @Test
  @DisplayName("findAllByChannelId_성공")
  void findAllByChannelId_WithExistingChannelIdAndCursor_ShouldReturnSliceMessage() {
    // given
    User author = createTestUser("유저", "user@email.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널", "테스트 채널입니다.");

    Instant now = Instant.now();
    Instant fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES);
    Instant tenMinutesAgo = now.minus(10, ChronoUnit.MINUTES);

    Message message1 = createTestMessage(author, channel, "첫 번째 메시지", tenMinutesAgo);
    Message message2 = createTestMessage(author, channel, "두 번째 메시지", fiveMinutesAgo);
    Message message3 = createTestMessage(author, channel, "세 번째 메시지", now);

    entityManager.flush();
    entityManager.clear();

    // when - 최신 메시지보다 이전 시간으로 조회
    Slice<Message> messages = messageRepository.findAllByChannelId(
        channel.getId(),
        now.plus(1, ChronoUnit.MINUTES),  // 현재 시간보다 더 미래
        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    // then
    List<Message> content = messages.getContent();
    Message firstMessage = content.get(0);
    assertThat(messages).isNotNull();
    assertThat(messages.hasContent()).isTrue();
    assertThat(messages.getNumberOfElements()).isEqualTo(2);  // 페이지 크기 만큼만 반환
    assertThat(messages.hasNext()).isTrue();
    assertThat(firstMessage.getContent()).isEqualTo("세 번째 메시지");
    assertThat(firstMessage.getChannel().getId()).isEqualTo(channel.getId());
    assertThat(firstMessage.getCreatedAt()).isAfterOrEqualTo(
        content.get(1).getCreatedAt()); // 정렬 기준 적용되었는지 확인

    // 저자 정보가 함께 로드되었는지 확인 (FETCH JOIN)
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor())).isTrue();
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor().getStatus())).isTrue();
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor().getProfile())).isTrue();
  }

  @Test
  @DisplayName("findAllByChannelId_유효하지 않은 cursor 값")
  void findAllByChannelId_WithInvalidCursor_ShouldReturnEmptyContent() {
    // given
    User author = createTestUser("유저", "user@email.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널", "테스트 채널입니다.");
    Message message = createTestMessage(author, channel, "테스트입니다.", Instant.now());

    entityManager.flush();
    entityManager.clear();

    // when
    Slice<Message> messages = messageRepository.findAllByChannelId(
        channel.getId(),
        Instant.parse("2020-06-01T12:00:00Z"),
        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    // then
    assertThat(messages).isNotNull();
    assertThat(messages.getContent()).isEmpty();
  }

  @Test
  @DisplayName("findLatestCreatedAtByChannelId_성공")
  void findLatestCreatedAtByChannelId_ReturnsLastMessageTime() {
    // given
    User author = createTestUser("testUser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널", "테스트채널입니다.");

    Instant now = Instant.now();
    Instant fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES);
    Instant tenMinutesAgo = now.minus(10, ChronoUnit.MINUTES);

    createTestMessage(author, channel, "첫 번째 메시지", tenMinutesAgo);
    createTestMessage(author, channel, "두 번째 메시지", fiveMinutesAgo);
    Message lastMessage = createTestMessage(author, channel, "세 번째 메시지", now);

    entityManager.flush();
    entityManager.clear();

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLatestCreatedAtByChannelId(
        channel.getId());

    // then
    assertThat(lastMessageAt).isPresent();
    // 마지막 메시지 시간과 일치하는지 확인 (밀리초 단위 이하의 차이는 무시)
    assertThat(lastMessageAt.get().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(lastMessage.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  @DisplayName("findLatestCreatedAtByChannelId_메시지가 없는 채널일 때 빈 값 반환")
  void findLatestCreatedAtByChannelId_WithNoMessages_ShouldReturnEmpty() {
    // given
    Channel emptyChannel = createTestChannel(ChannelType.PUBLIC, "빈채널", "빈채널입니다.");

    entityManager.flush();
    entityManager.clear();

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLatestCreatedAtByChannelId(
        emptyChannel.getId()
    );

    // then
    assertThat(lastMessageAt).isEmpty();
  }

  @Test
  @DisplayName("Delete All Messages By ChannelId_성공")
  void deleteAllByChannelId_DeletesAllMessages() {
    // given
    User author = createTestUser("testUser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널", "테스트채널1");
    Channel otherChannel = createTestChannel(ChannelType.PUBLIC, "다른채널", "테스트채널2");

    createTestMessage(author, channel, "첫 번째 메시지", null);
    createTestMessage(author, channel, "두 번째 메시지", null);
    createTestMessage(author, channel, "세 번째 메시지", null);

    createTestMessage(author, otherChannel, "다른 채널 메시지", null);

    entityManager.flush();
    entityManager.clear();

    // when
    messageRepository.deleteAllByChannelId(channel.getId());
    entityManager.flush();
    entityManager.clear();

    // then
    // 해당 채널의 메시지는 삭제되었는지 확인
    List<Message> channelMessages = messageRepository.findAllByChannelId(
        channel.getId(),
        Instant.now().plus(1, ChronoUnit.DAYS),
        PageRequest.of(0, 100)
    ).getContent();
    assertThat(channelMessages).isEmpty();

    // 다른 채널의 메시지는 그대로인지 확인
    List<Message> otherChannelMessages = messageRepository.findAllByChannelId(
        otherChannel.getId(),
        Instant.now().plus(1, ChronoUnit.DAYS),
        PageRequest.of(0, 100)
    ).getContent();
    assertThat(otherChannelMessages).hasSize(1);
  }

}
