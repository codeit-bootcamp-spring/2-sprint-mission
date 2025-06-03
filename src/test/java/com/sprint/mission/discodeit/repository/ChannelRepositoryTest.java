package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.util.Arrays;
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
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 채널 생성
   */
  private Channel createTestChannel(ChannelType type, String name, String description) {
    Channel channel = Channel.create(type, name, description);
    return channelRepository.save(channel);
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn_성공")
  void findAllByTypeOrIdIn_WithExistingIds_ShouldReturnChannelList() {
    // given
    Channel channel1 = createTestChannel(ChannelType.PUBLIC, "퍼블릭채널", "퍼블릭채널입니다.");
    Channel channel2 = createTestChannel(ChannelType.PRIVATE, null, null);
    Channel channel3 = createTestChannel(ChannelType.PRIVATE, null, null);
    Channel excludedChannel = createTestChannel(ChannelType.PRIVATE, null, null);

    channelRepository.saveAll(Arrays.asList(channel1, channel2, channel3));

    entityManager.persist(excludedChannel);
    entityManager.flush();
    entityManager.clear();

    // when
    List<UUID> privateChannelIds = List.of(channel2.getId(), channel3.getId());
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        privateChannelIds);

    // then
    assertThat(channels).hasSize(3);
    assertThat(channels).extracting("id").containsExactlyInAnyOrder(
        channel1.getId(), channel2.getId(), channel3.getId()
    );
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn_실패_조건에 맞는 채널 없을 때")
  void findAllByTypeOrIdIn_WithNoMatchingConditions_ShouldReturnEmptyList() {
    // given
    Channel privateChannel1 = createTestChannel(ChannelType.PRIVATE, null, null);
    Channel privateChannel2 = createTestChannel(ChannelType.PRIVATE, null, null);

    channelRepository.saveAll(Arrays.asList(privateChannel1, privateChannel2));

    entityManager.flush();
    entityManager.clear();

    List<UUID> nonMatchingIds = List.of(UUID.randomUUID(), UUID.randomUUID());

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        nonMatchingIds);

    // then
    assertThat(result).isEmpty();
  }


}
