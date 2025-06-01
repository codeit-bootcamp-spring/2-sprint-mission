package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
public class JpaMessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  EntityManager entityManager;

  @Autowired
  JdbcTemplate jdbcTemplate;

  private Channel channel1;
  private User user1;
  private User user2;
  private BinaryContent binaryContent1;
  private BinaryContent binaryContent2;
  private BinaryContent binaryContent3;
  Instant createdAt1 = Instant.parse("2025-05-22T10:00:00Z");
  Instant createdAt2 = Instant.parse("2025-05-22T12:00:00Z");
  Instant createdAt3 = Instant.parse("2025-05-22T15:00:00Z");

  @BeforeEach
  void setUp() {
    channel1 = Channel.builder()
        .name("test1")
        .type(ChannelType.PUBLIC)
        .description("testChannel1")
        .build();
    entityManager.persist(channel1);

    user1 = User.builder()
        .username("test1")
        .email("test1@test.com")
        .password("1234")
        .build();
    entityManager.persist(user1);

    user2 = User.builder()
        .username("test2")
        .email("test2@test.com")
        .password("1234")
        .build();
    entityManager.persist(user2);

    binaryContent1 = BinaryContent.builder()
        .filename("file1")
        .contentType("img/png")
        .size(5)
        .build();
    entityManager.persist(binaryContent1);

    binaryContent2 = BinaryContent.builder()
        .filename("file2")
        .contentType("img/png")
        .size(5)
        .build();
    entityManager.persist(binaryContent2);

    binaryContent3 = BinaryContent.builder()
        .filename("file3")
        .contentType("img/png")
        .size(5)
        .build();
    entityManager.persist(binaryContent3);

    entityManager.flush();
    entityManager.clear();

    String sql = "INSERT INTO messages (id, content, channel_id, author_id, created_at) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, UUID.randomUUID(), "testMessage1", channel1.getId(), user1.getId(),
        Timestamp.from(createdAt1));
    jdbcTemplate.update(sql, UUID.randomUUID(), "testMessage2", channel1.getId(), user2.getId(),
        Timestamp.from(createdAt2));
    jdbcTemplate.update(sql, UUID.randomUUID(), "testMessage3", channel1.getId(), user2.getId(),
        Timestamp.from(createdAt3));

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("커서가 없을 때, 채널 별 메시지 페이징 조회 성공")
  void findAll_by_channelId_without_cursor() {
    // given
    UUID channelId = channel1.getId();
    Pageable pageable = PageRequest.of(0, 2);

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdInitial(channelId, pageable);

    // then
    assertThat(messages.getContent()).hasSize(2);
    assertThat(messages.hasNext()).isTrue();
  }

  @Test
  @DisplayName("커서가 있을 때, 채널 별 메시지 페이징 조회 성공")
  void findAll_by_channelId_with_cursor() {
    // given
    Instant cursor = createdAt3;
    UUID channelId = channel1.getId();
    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt", "id"));

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdAfterCursor(channelId, cursor,
        pageable);

    // then
    assertThat(messages.hasNext()).isFalse();
    assertThat(messages.getSize()).isEqualTo(2);
  }


  @Test
  @DisplayName("채널 별 메시지 중 가장 최근에 만들어진 메시지 시간 조회 성공")
  void find_latestMessageTime_by_channelId_success() {
    // given
    UUID channelId = channel1.getId();

    // when
    Optional<Instant> latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(
        channelId);

    // then
    assertThat(latestMessageTime).isPresent();
    assertThat(latestMessageTime.get().truncatedTo(ChronoUnit.SECONDS)).isEqualTo(
        createdAt3.truncatedTo(ChronoUnit.SECONDS));
  }
}
