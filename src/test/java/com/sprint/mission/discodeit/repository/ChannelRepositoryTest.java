package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  ChannelRepository channelRepository;

  @Test
  void findAllByTypeOrIdIn_success() {
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "공지", "설명");
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.saveAll(List.of(publicChannel, privateChannel));

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC, List.of(privateChannel.getId()));

    assertEquals(2, result.size());
  }

  @Test
  void findAllByTypeOrIdIn_failed() {
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PRIVATE, List.of());

    assertTrue(result.isEmpty());
  }
}

