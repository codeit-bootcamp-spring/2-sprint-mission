package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChannelServiceTest {
    private static final String NAME = "7팀";
    private ChannelService channelService;
    private ChannelDto setUpChannel;

    @BeforeEach
    void setUp() {
        channelService = JCFChannelService.getInstance(); // TODO: 2/26/25 싱글톤이 테스트 목립적인지 확인 필요
        setUpChannel = channelService.create(NAME, new UserDto(UUID.randomUUID(), "황지환"));
    }

    @Test
    void 채널_생성() {
        assertThat(setUpChannel.name()).isEqualTo(NAME);
    }

    @Test
    void 채널_단건_조회() {
        ChannelDto channel = channelService.findById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @Test
    void 채널_이름_수정() {
        String name = "7팀 스터디";
        channelService.updateName(setUpChannel.id(), name);

        assertThat(channelService.findById(setUpChannel.id()).name())
                .isEqualTo(name);
    }

    @Test
    void 채널_삭제() {
        UUID id = setUpChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}