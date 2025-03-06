package com.sprint.mission.discodeit.service;

import static com.sprint.mission.discodeit.config.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.config.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.infra.UserRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChannelServiceTest {
    private ChannelService channelService;
    private ChannelDto setUpChannel;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(
                new User(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()));

        channelService = new JCFChannelService(new JCFChannelRepository(), new JCFUserService(userRepository));
        setUpChannel = channelService.create(CHANNEL_NAME,
                new UserDto(user.getId(), LONGIN_USER.getName(), LONGIN_USER.getEmail()));
    }

    @Test
    void 채널_생성() {
        assertThat(setUpChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @Test
    void 채널_단건_조회() {
        ChannelDto channel = channelService.findById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @Test
    void 채널_이름_수정() {
        channelService.updateName(setUpChannel.id(), UPDATED_CHANNEL_NAME);

        assertThat(channelService.findById(setUpChannel.id()).name())
                .isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @Test
    void 채널_삭제() {
        UUID id = setUpChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}