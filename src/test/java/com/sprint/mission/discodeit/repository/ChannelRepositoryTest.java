package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;
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
  private TestEntityManager em;

  @Test
  public void findAllByTypeOrIdIn_shouldReturnChannels_matchingTypeOrIds() {
    Channel publicChannel1 = createChannel(ChannelType.PUBLIC, "public1", "public1");
    Channel publicChannel2 = createChannel(ChannelType.PUBLIC, "public2", "public2");
    Channel privateChannel1 = createChannel(ChannelType.PRIVATE, null, null);
    em.flush();
    em.clear();

    List<UUID> channelIds = List.of(privateChannel1.getId());
    List<Channel> res = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds);

    assertThat(res).extracting(Channel::getId)
        .containsExactlyInAnyOrder(
            publicChannel1.getId(),
            publicChannel2.getId(),
            privateChannel1.getId());
  }

  private Channel createChannel(ChannelType type, String name, String description) {
    Channel channel = new Channel(type, name, description);
    em.persist(channel);
    return channel;
  }
}
