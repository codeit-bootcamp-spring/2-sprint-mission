package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  TestEntityManager entityManager;

  @Test
  void findAllByTypeOrIdIn_success() {
    Channel publicChannel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "public", "desc"));
    Channel privateChannel1 = channelRepository.save(
        new Channel(ChannelType.PRIVATE, "private1", "desc"));
    Channel privateChannel2 = channelRepository.save(
        new Channel(ChannelType.PRIVATE, "private2", "desc"));

    entityManager.flush();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE,
        List.of(publicChannel.getId()));

    assertThat(result).hasSize(3);
  }
}