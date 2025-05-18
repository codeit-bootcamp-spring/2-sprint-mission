package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("findAllByTypeOrIdIn_성공")
  void findAllByTypeOrIdIn_WithExistingIds_ShouldReturnChannelList() {
    // TestEntityManager를 사용하여 테스트 데이터 생성
    Channel channel1 = Channel.create(ChannelType.PUBLIC, "퍼블릭채널", "퍼블릭채널입니다.");
    Channel channel2 = Channel.create(ChannelType.PRIVATE, null, null);
    Channel channel3 = Channel.create(ChannelType.PRIVATE, null, null);
    Channel excludedChannel = Channel.create(ChannelType.PRIVATE, null, null);
    entityManager.persist(channel1);
    entityManager.persist(channel2);
    entityManager.persist(channel3);
    entityManager.persist(excludedChannel);
    entityManager.flush();

    List<UUID> privateChannelIds = List.of(channel2.getId(), channel3.getId());

    // 테스트 대상 메서드 실행
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        privateChannelIds);

    // 검증
    assertThat(channels).hasSize(3);
    assertThat(channels).extracting("id").containsExactlyInAnyOrder(
        channel1.getId(), channel2.getId(), channel3.getId()
    );
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn_실패_조건에 맞는 채널 없을 때")
  void findAllByTypeOrIdIn_WithNoMatchingConditions_ShouldReturnEmptyList() {
    // given
    Channel privateChannel1 = Channel.create(ChannelType.PRIVATE, null, null);
    Channel privateChannel2 = Channel.create(ChannelType.PRIVATE, null, null);
    entityManager.persist(privateChannel1);
    entityManager.persist(privateChannel2);

    List<UUID> nonMatchingIds = List.of(UUID.randomUUID(), UUID.randomUUID());

    entityManager.flush();

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        nonMatchingIds);

    // then
    assertThat(result).isEmpty();
  }


}
