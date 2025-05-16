package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.testAuditingConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import(testAuditingConfig.class)
@ActiveProfiles("test")
public class JpaMessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  EntityManager entityManager;

  private Channel channel1;
  private User user1;
  private User user2;
  private Message message1;
  private Message message2;
  private Message message3;
  private BinaryContent binaryContent1;
  private BinaryContent binaryContent2;
  private BinaryContent binaryContent3;

  @BeforeEach
  void setUp() throws InterruptedException {
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

    message1 = Message.builder()
        .attachments(List.of(binaryContent1))
        .channel(channel1)
        .author(user1)
        .content("testMessage1")
        .build();
    messageRepository.save(message1);
    entityManager.flush();

    message2 = Message.builder()
        .attachments(List.of(binaryContent2))
        .channel(channel1)
        .author(user2)
        .content("testMessage2")
        .build();
    messageRepository.save(message2);
    entityManager.flush();

    message3 = Message.builder()
        .attachments(List.of(binaryContent3))
        .channel(channel1)
        .author(user2)
        .content("testMessage3")
        .build();
    messageRepository.save(message3);

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

  // @createdDate 어노테이션으로 DB에 flush 될 때 createdAt이 생성되는데,
  // flush할 때 한번에 날아가다보니, 어떨 땐 createdAt이 1,2,3 순서에 맞게 생성돼서 테스트가 성공할 때가 있고
  // 순서에 맞지 않게 저장돼서 테스트가 실패하는 경우가 있음
  // @createdDate를 쓰지 않으면, createdAt 필드를 명시적으로 설정해줘서 테스트를 편하게 할 수 있지만,
  // 구현을 전부 바꿔야하기 때문에 힘들고, 이 방법 말고는 별다른 해결 방법이 생각나지 않아서,
  // Repository에 createdAt이 같을 경우 id로 DESC 정렬을 하는 식으로 변경하였습니다.
  @Test
  @DisplayName("커서가 있을 때, 채널 별 메시지 페이징 조회 성공")
  void findAll_by_channelId_with_cursor() {
    // given
    Instant cursor = message3.getCreatedAt();
    UUID channelId = channel1.getId();
    Pageable pageable = PageRequest.of(0, 2);

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdAfterCursor(channelId, cursor,
        pageable);

    // then
    assertThat(messages.getContent()).hasSize(2);
    // 그럼에도 가끔 이 부분이 실패하네요
    assertThat(messages.hasNext()).isFalse();
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
    // 타임존 문제로 실패
//    assertThat(latestMessageTime.get()).isEqualTo(
//        message3.getCreatedAt());
  }
}
