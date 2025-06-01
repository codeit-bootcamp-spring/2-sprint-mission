package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(AppConfig.class)
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Channel privateChannel;
    private Channel publicChannel;

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();

        publicChannel = new Channel(ChannelType.PUBLIC, "Public Channel", "This is a public channel.");

        privateChannel = new Channel(ChannelType.PRIVATE, null, null);

        channelRepository.save(privateChannel);
        channelRepository.save(publicChannel);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("존재하는 채널")
    void existsByTypeAndName_success() {
        boolean exists = channelRepository.existsByTypeAndName(ChannelType.PUBLIC, "Public Channel");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 채널")
    void existsByTypeAndName_fail() {
        boolean exists = channelRepository.existsByTypeAndName(ChannelType.PRIVATE, "unknown");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("PUBLIC 타입으로 조회")
    void findAllByTypeOrIdIn_byType_success() {
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, Collections.emptyList());
        assertThat(result).hasSize(1);
        assertThat(result)
                .extracting(Channel::getId)
                .containsExactly(publicChannel.getId());
    }

    @Test
    @DisplayName("특정 ID 포함 조회")
    void findAllByTypeOrIdIn_byId_success() {
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(null,
                List.of(privateChannel.getId()));
        assertThat(result).hasSize(1);
        assertThat(result)
                .extracting(Channel::getId)
                .containsExactly(privateChannel.getId());
    }

    @Test
    @DisplayName("일치하는 조건 없음")
    void findAllByTypeOrIdIn_fail() {
        UUID nonExistentId = UUID.randomUUID();
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(null, List.of(nonExistentId));
        assertThat(result).isEmpty();
    }
}
