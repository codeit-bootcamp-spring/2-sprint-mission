package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChannelServiceTest {
    private ChannelService channelService;
    private ChannelDto setUpChannel;
    private User setUpUser;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        channelService = new BasicChannelService(new JCFChannelRepository(), new JCFReadStatusRepository());
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME, new UserDto(setUpUser.getId(), LOGIN_USER.getName(), LOGIN_USER.getEmail(), null, false));
        setUpChannel = channelService.create(channelRegisterDto);
    }

    @Test
    void 채널_생성() {
        assertThat(setUpChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("private 채널 ID로 조회된 채널을 반환합니다")
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

    @DisplayName("전체 조회시 public 채널과 요청한 유저가 속한 private 채널만 반환합니다.")
    @Test
    void findAll() {
        User otherUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PRIVATE, CHANNEL_NAME, new UserDto(otherUser.getId(), LOGIN_USER.getName(), LOGIN_USER.getEmail(), null, false));
        channelService.create(channelRegisterDto);

        ChannelRegisterDto setUpUserChannelRegisterDto = new ChannelRegisterDto(ChannelType.PRIVATE, CHANNEL_NAME, new UserDto(setUpUser.getId(), LOGIN_USER.getName(), LOGIN_USER.getEmail(), null, false));
        ChannelDto setUpUserPrivateChannel = channelService.create(setUpUserChannelRegisterDto);

        List<UUID> setUpUserChannelIds = channelService.findAllByUserId(setUpUser.getId())
                .stream()
                .map(ChannelDto::id)
                .toList();

        assertThat(setUpUserChannelIds).containsExactlyInAnyOrder(setUpChannel.id(), setUpUserPrivateChannel.id());
    }


    @Test
    void 채널_삭제() {
        UUID id = setUpChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}