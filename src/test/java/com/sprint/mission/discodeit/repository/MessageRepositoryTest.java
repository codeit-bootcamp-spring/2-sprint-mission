package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("findAllByChannelId_성공")
  void findAllByChannelId_WithExistingChannelIdAndCursor_ShouldReturnSliceMessage() {
    // TestEntityManager를 사용하여 테스트 데이터 생성
    User author = User.create("유저", "user@email.com", "user1234!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "테스트채널", "테스트 채널입니다.");
    entityManager.persist(author);
    entityManager.persist(channel);
    entityManager.flush();

    Message message = Message.create(author, channel, "테스트입니다.", null);
    entityManager.persist(message);
    entityManager.flush();

    UUID channelId = channel.getId();
    Instant cursor = Instant.parse("2025-06-01T12:00:00Z");
    Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

    // 테스트 대상 메서드 실행
    Slice<Message> messages = messageRepository.findAllByChannelId(channelId, cursor, pageable);

    // 검증
    assertThat(messages).isNotNull();
    assertThat(messages.getContent()).isNotEmpty();
    assertThat(messages.getContent().get(0).getContent()).isEqualTo("테스트입니다.");
    assertThat(messages.getContent().get(0).getChannel().getId()).isEqualTo(channelId);
  }

  @Test
  @DisplayName("findAllByChannelId_실패_유효하지 않은 cursor 값")
  void findAllByChannelId_WithInvalidCursor_ShouldReturnEmptyContent() {
    // TestEntityManager를 사용하여 테스트 데이터 생성
    User author = User.create("유저", "user@email.com", "user1234!", null);
    Channel channel = Channel.create(ChannelType.PUBLIC, "테스트채널", "테스트 채널입니다.");
    entityManager.persist(author);
    entityManager.persist(channel);
    entityManager.flush();

    Message message = Message.create(author, channel, "테스트입니다.", null);
    entityManager.persist(message);
    entityManager.flush();

    UUID channelId = channel.getId();
    Instant cursor = Instant.parse("2020-06-01T12:00:00Z");
    Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

    // 테스트 대상 메서드 실행
    Slice<Message> messages = messageRepository.findAllByChannelId(channelId, cursor, pageable);

    // 검증
    assertThat(messages).isNotNull();
    assertThat(messages.getContent()).isEmpty();
  }

}
