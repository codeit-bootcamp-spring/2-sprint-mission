package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("특정 채널의 메세지들을 시간순으로 조회 테스트 - 성공")
  void findAllByChannelIdWithAuthor_success() {
    // given
    BinaryContent profile = new BinaryContent("profile.png", 123L, "image/png");
    User user = new User("author1", "author1@email.com", "password", profile);
    new UserStatus(user, Instant.now());
    userRepository.save(user);

    Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "설명");
    channelRepository.save(channel);

    Instant baseTime = Instant.now().minusSeconds(60); // 60초 전부터 시작
    IntStream.range(0, 6).forEach(i -> {
      Message message = new Message("메시지 " + i, channel, user, List.of());
      messageRepository.save(message);
      try {
        Thread.sleep(100); // 10ms 간격
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    Instant standardTime = Instant.now().plusSeconds(10); // 모든 메시지가 포함되도록

    // when
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        standardTime, pageable);

    // then
    assertThat(result).hasSize(5); // 첫 페이지에 5개
    assertThat(result.isFirst()).isTrue();
    assertThat(result.hasNext()).isTrue(); // 총 6개 → 다음 페이지 있음

    List<Message> content = result.getContent();

    // 정렬 순서 검증: 최신순 (내림차순)
    for (int i = 1; i < content.size(); i++) {
      Instant prev = content.get(i - 1).getCreatedAt();
      Instant curr = content.get(i).getCreatedAt();
      assertThat(prev).isAfterOrEqualTo(curr); // prev ≥ curr
    }

    // 각 메시지 연관 객체 검증
    assertThat(content).allSatisfy(message -> {
      assertThat(message.getAuthor()).isNotNull();
      assertThat(message.getAuthor().getUsername()).isEqualTo("author1");
      assertThat(message.getAuthor().getStatus()).isNotNull();
      assertThat(message.getAuthor().getProfile()).isNotNull();
      assertThat(message.getChannel()).isNotNull();
      assertThat(message.getChannel().getName()).isEqualTo("테스트 채널");
    });

    // 메시지 내용 순서 확인
    assertThat(content.get(0).getContent()).isEqualTo("메시지 5");
    assertThat(content.get(1).getContent()).isEqualTo("메시지 4");
    assertThat(content.get(2).getContent()).isEqualTo("메시지 3");
    assertThat(content.get(3).getContent()).isEqualTo("메시지 2");
    assertThat(content.get(4).getContent()).isEqualTo("메시지 1");
  }

  @Test
  @DisplayName("존재하지 않는 채널 ID로 조회 시 빈 결과를 반환한다")
  void findAllByChannelIdWithAuthor_fail_doesnt_exist_channel() {
    // given
    UUID nonexistentChannelId = UUID.randomUUID();
    Instant standardTime = Instant.now();

    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(nonexistentChannelId, standardTime, pageable);

    // then
    assertThat(result).isEmpty();
    assertThat(result.hasNext()).isFalse();
  }
}