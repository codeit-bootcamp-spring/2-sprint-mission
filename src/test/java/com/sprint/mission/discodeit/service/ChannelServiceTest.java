package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.application.ChannelDto;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChannelServiceTest {
    private ChannelService channelService;
    private ChannelDto initChannel;
    private static final String NAME = "7팀";

    @BeforeEach
    void setUp() {
        channelService = new JCFChannelService();
        initChannel = channelService.create(NAME);
    }

    @Test
    void 채널_생성() {
        assertThat(initChannel.name()).isEqualTo(NAME);
    }

    @Test
    void 채널_단건_조회() {
        ChannelDto channel = channelService.findById(initChannel.id());
        assertThat(initChannel.id() + initChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @Test
    void 채널_이름_수정() {
        String name = "7팀 스터디";
        channelService.updateName(initChannel.id(), name);

        assertThat(channelService.findById(initChannel.id()).name())
                .isEqualTo(name);
    }

    @Test
    void 채널_삭제() {
        UUID id = initChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}