package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@EntityScan(basePackages = "com.sprint.mission.discodeit.entity")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @DisplayName("타입으로 채널 조회 - 성공")
  @Test
  void findAllByTypeSuccess() {
    // given
    Channel channel1 = new Channel("test1", "test", ChannelType.PUBLIC);
    Channel channel2 = new Channel("", "", ChannelType.PRIVATE);
    Channel channel3 = new Channel("test2", "test", ChannelType.PUBLIC);

    channelRepository.save(channel1);
    channelRepository.save(channel2);
    channelRepository.save(channel3);

    // when
    List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting("name").containsExactlyInAnyOrder("test1", "test2");
  }

  @DisplayName("타입으로 채널 조회 - 실패 (존재하지 않음)")
  @Test
  void findAllByTypeEmpty() {
    // given
    channelRepository.save(new Channel("", "", ChannelType.PRIVATE));

    // when
    List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);

    // then
    assertThat(result).isEmpty();
  }
}
