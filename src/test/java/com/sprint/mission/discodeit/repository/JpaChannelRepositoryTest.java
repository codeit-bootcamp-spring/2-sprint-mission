package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
public class JpaChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;


  @Test
  @DisplayName("channelId List에 해당하는 Channel 찾기 성공")
  void findAll_by_type_or_in_ChannelIdList_success() {
    // given
    Channel channel1 = Channel.builder()
        .type(ChannelType.PUBLIC)
        .build();
    channelRepository.save(channel1);

    Channel channel2 = Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
    channelRepository.save(channel2);

    List<UUID> channelIds = List.of(channel2.getId());

    // when
    List<Channel> channelResults = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        channelIds);

    // then
    assertThat(channelResults.size()).isEqualTo(2);
    assertThat(channelResults.stream().map(BaseEntity::getId).toList())
        .containsExactlyInAnyOrderElementsOf(
            channelResults.stream().map(BaseEntity::getId).toList());
  }

}
