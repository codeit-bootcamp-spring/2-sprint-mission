package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("findAllByTypeOrIdIn — PUBLIC 타입만 조회")
  void testFindAllByTypeOrIdIn_PublicOnly() {
    Channel pub1 = new Channel(ChannelType.PUBLIC, "Public1", "Desc1");
    Channel priv1 = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.saveAll(List.of(pub1, priv1));

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    assertThat(result)
        .hasSize(1)
        .allSatisfy(ch -> assertThat(ch.getType()).isEqualTo(ChannelType.PUBLIC));
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn — 특정 ID 포함하여 조회")
  void testFindAllByTypeOrIdIn_ById() {
    Channel pub1 = new Channel(ChannelType.PUBLIC, "Public1", "Desc1");
    Channel priv1 = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.saveAll(List.of(pub1, priv1));
    UUID privId = priv1.getId();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(privId));
    
    assertThat(result)
        .hasSize(2)
        .extracting(Channel::getId)
        .containsExactlyInAnyOrder(pub1.getId(), privId);
  }
}
