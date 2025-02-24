package com.sprint.mission.discodeit.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.application.ChannelDto;
import com.sprint.mission.discodeit.jcf.JCFChannelRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChannelRepositoryTest {
    private static final String NAME = "7팀";
    private ChannelRepository channelRepository;
    private ChannelDto setUpChannel;

    @BeforeEach
    void setUp() {
        channelRepository = new JCFChannelRepository();
        setUpChannel = channelRepository.save(NAME);
    }

    @Test
    void 채널_생성() {
        assertThat(setUpChannel.name()).isEqualTo(NAME);
    }

    @Test
    void 채널_단건_조회() {
        ChannelDto channel = channelRepository.findById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @Test
    void 채널_이름_수정() {
        String name = "7팀 스터디";
        channelRepository.updateName(setUpChannel.id(), name);

        assertThat(channelRepository.findById(setUpChannel.id()).name())
                .isEqualTo(name);
    }

    @Test
    void 채널_삭제() {
        UUID id = setUpChannel.id();
        channelRepository.delete(id);

        assertThatThrownBy(() -> channelRepository.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}