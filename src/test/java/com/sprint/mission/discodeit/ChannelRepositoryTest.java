package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("채널 저장 및 조회 테스트")
  void saveAndFindChannel() {
    Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "test description");
    channelRepository.save(channel);

    List<Channel> foundChannels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(channel.getId()));

    assertThat(foundChannels).isNotEmpty();
    assertThat(foundChannels.get(0).getName()).isEqualTo("testChannel");
  }

  @Test
  @DisplayName("빈 결과 반환 테스트")
  void findAllByTypeOrIdIn_emptyResult() {
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE,
        List.of(UUID.randomUUID()));
    assertThat(channels).isEmpty();
  }
}
