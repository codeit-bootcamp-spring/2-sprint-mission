package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

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
  private BinaryContentRepository binaryContentRepository;

  @Test
  @DisplayName("findLastMessageAtByChannelId - 메시지가 있을 때 가장 최근 createdAt 반환")
  void testFindLastMessageAtExists() {
    Channel ch = new Channel(ChannelType.PUBLIC, "chan", "desc");
    channelRepository.save(ch);

    User u = new User("user", "u@test.com", "pw", null);
    userRepository.save(u);
    userStatusRepository.save(new UserStatus(u, u.getCreatedAt()));

    Message m1 = new Message("first", ch, u, List.of());
    Message m2 = new Message("second", ch, u, List.of());
    messageRepository.saveAll(List.of(m1, m2));

    Optional<Instant> last = messageRepository.findLastMessageAtByChannelId(ch.getId());

    assertThat(last).isPresent()
        .get().isEqualTo(m2.getCreatedAt());
  }

  @Test
  @DisplayName("findLastMessageAtByChannelId - 메시지가 없을 때 빈 Optional 반환")
  void testFindLastMessageAtNotExists() {
    Optional<Instant> last = messageRepository.findLastMessageAtByChannelId(UUID.randomUUID());

    assertThat(last).isEmpty();
  }

  @Test
  @DisplayName("findAllByChannelIdWithAuthor - 페이징 슬라이스 조회 성공")
  void testFindAllByChannelIdWithAuthor() {
    Channel ch = new Channel(ChannelType.PUBLIC, "chan", "desc");
    channelRepository.save(ch);

    User u = new User("user", "u@test.com", "pw", null);
    userRepository.save(u);
    userStatusRepository.save(new UserStatus(u, u.getCreatedAt()));

    Message m1 = new Message("msg1", ch, u, List.of());
    Message m2 = new Message("msg2", ch, u, List.of());
    messageRepository.saveAll(List.of(m1, m2));

    PageRequest pageable = PageRequest.of(0, 1);

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        ch.getId(), Instant.now().plusSeconds(10), pageable
    );

    assertThat(slice.getContent()).hasSize(1);
    assertThat(slice.hasNext()).isTrue();
    assertThat(slice.getContent().get(0).getContent()).isEqualTo("msg1");
  }

  @Test
  @DisplayName("findAllByChannelIdWithAuthor - 메시지 없을 때 빈 슬라이스 반환")
  void testFindAllByChannelIdWithAuthor_Empty() {
    UUID randomId = UUID.randomUUID();
    PageRequest pageable = PageRequest.of(0, 10);

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        randomId, Instant.now(), pageable
    );

    assertThat(slice.getContent()).isEmpty();
    assertThat(slice.hasNext()).isFalse();
  }

  @Test
  @DisplayName("deleteAllByChannelId - 특정 채널 메시지 전체 삭제")
  void testDeleteAllByChannelId() {
    Channel ch = new Channel(ChannelType.PUBLIC, "chan", "desc");
    channelRepository.save(ch);

    User u = new User("user", "u@test.com", "pw", null);
    userRepository.save(u);
    userStatusRepository.save(new UserStatus(u, u.getCreatedAt()));

    Message m = new Message("toDelete", ch, u, List.of());
    messageRepository.save(m);
    
    messageRepository.deleteAllByChannelId(ch.getId());

    assertThat(messageRepository.findById(m.getId())).isEmpty();
  }
}
