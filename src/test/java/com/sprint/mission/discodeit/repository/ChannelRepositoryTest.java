package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("findAllByTypeOrIdIn - 공개 채널 검색 (성공)")
    void findAllByTypeOrIdIn_success_public() {
        // given
        Channel publicChannel = new Channel(ChannelType.PUBLIC, "공개채널", "public");
        channelRepository.save(publicChannel);

        Channel privateChannel = new Channel(ChannelType.PRIVATE, "비공개채널", "private");
        channelRepository.save(privateChannel);

        // when
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC, List.of(privateChannel.getId())
        );

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("type")
                .contains(ChannelType.PUBLIC, ChannelType.PRIVATE);
    }

    @Test
    @DisplayName("findAllByTypeOrIdIn - 일치하는 채널이 없는 경우 (실패)")
    void findAllByTypeOrIdIn_fail() {
        // given
        Channel privateChannel = new Channel(ChannelType.PRIVATE, "비공개채널", "private");
        channelRepository.save(privateChannel);

        // when
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC, List.of(UUID.randomUUID())
        );

        // then
        assertThat(result).isEmpty();
    }
}
