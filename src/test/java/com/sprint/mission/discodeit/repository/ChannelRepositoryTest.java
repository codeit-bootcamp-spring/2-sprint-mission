package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("채널 타입 또는 ID 목록으로 조회 성공")
  public void testFindAllByTypeOrIdIn_Success() {
    Channel publicChannel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "General", "공용 채널입니다"));
    Channel privateChannel = channelRepository.save(
        new Channel(ChannelType.PRIVATE, "Secret", "비공개 채널입니다"));

    entityManager.flush();
    entityManager.clear();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC,
        List.of(privateChannel.getId())
    );

    assertThat(result).hasSize(2);
    assertThat(result).extracting("id")
        .containsExactlyInAnyOrder(publicChannel.getId(), privateChannel.getId());
  }

  @Test
  @DisplayName("조건에 맞는 채널이 없는 경우 빈 값 반환")
  public void testFindAllByTypeOrIdIn_Empty() {
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE,
        List.of(UUID.randomUUID()));

    assertThat(result).isEmpty();
  }
}
