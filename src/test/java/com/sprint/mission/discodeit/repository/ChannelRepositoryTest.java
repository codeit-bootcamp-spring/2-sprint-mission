package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class ChannelRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("채널 타입으로 조회 - PUBLIC 채널만 반환")
    void findAllByTypeOrIdIn_withType_only() {
        Channel publicChannel = new Channel(ChannelType.PUBLIC, "공개 채널", "설명1");
        Channel privateChannel = new Channel(ChannelType.PRIVATE, "비공개 채널", "설명2");

        entityManager.persist(publicChannel);
        entityManager.persist(privateChannel);
        entityManager.flush();
        entityManager.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(ChannelType.PUBLIC);
    }
    @Test
    @DisplayName("ID 리스트로 조회 - 해당 ID의 채널만 반환")
    void findAllByTypeOrIdIn_withIdList_only() {
        Channel channel1 = new Channel(ChannelType.PUBLIC, "채널1", "설명1");
        Channel channel2 = new Channel(ChannelType.PUBLIC, "채널2", "설명2");
        Channel channel3 = new Channel(ChannelType.PRIVATE, "채널3", "설명3");

        entityManager.persist(channel1);
        entityManager.persist(channel2);
        entityManager.persist(channel3);
        entityManager.flush();
        entityManager.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PRIVATE,
                List.of(channel2.getId(), channel3.getId())
        );

        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("채널2", "채널3");
    }
    @Test
    @DisplayName("type 또는 id 리스트 조건에 해당하는 채널 모두 조회")
    void findAllByTypeOrIdIn_withTypeAndIdList() {
        Channel matchByType = new Channel(ChannelType.PUBLIC, "타입 매치", "desc");
        Channel matchById = new Channel(ChannelType.PRIVATE, "ID 매치", "desc");
        Channel matchBoth = new Channel(ChannelType.PUBLIC, "둘 다 매치", "desc");
        Channel noMatch = new Channel(ChannelType.PRIVATE, "불일치", "desc");

        entityManager.persist(matchByType);
        entityManager.persist(matchById);
        entityManager.persist(matchBoth);
        entityManager.persist(noMatch);

        entityManager.flush();
        entityManager.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC,
                List.of(matchById.getId(), matchBoth.getId())
        );

        assertThat(result).hasSize(3);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("타입 매치", "ID 매치", "둘 다 매치");
    }
    @Test
    @DisplayName("조회 조건에 해당하는 채널이 없을 경우 빈 리스트 반환")
    void findAllByTypeOrIdIn_noMatch_returnsEmptyList() {
        Channel channel1 = new Channel(ChannelType.PUBLIC, "채널1", "desc");
        Channel channel2 = new Channel(ChannelType.PUBLIC, "채널2", "desc");

        entityManager.persist(channel1);
        entityManager.persist(channel2);
        entityManager.flush();
        entityManager.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PRIVATE,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        assertThat(result).isEmpty();
    }
}
