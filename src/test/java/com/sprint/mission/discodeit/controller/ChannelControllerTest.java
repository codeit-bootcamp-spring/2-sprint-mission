package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

class ChannelControllerTest {
    private ChannelController channelController;
    private UserService userService;
    private ChannelService channelService;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private MessageRepository messageRepository;
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private UserDto owner;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        messageRepository = new JCFMessageRepository();
        channelRepository = new JCFChannelRepository();
        readStatusRepository = new JCFReadStatusRepository();

        userService = new BasicUserService(userRepository, userStatusRepository);
        channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
        channelController = new ChannelController(channelService, userService, new BasicReadStatusService(readStatusRepository, channelRepository, userRepository));

        owner = userService.register(new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @DisplayName("Private 채널 조회 시 유저의 세부 정보를 반환한다.")
    @Test
    void findByIdPrivateChannel() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        ChannelResponseDto channel = createPrivateChannel(owner);

        ChannelResponseDto channelResponseDto = channelController.addMemberToPrivate(channel.id(), OTHER_USER.getEmail());
        List<String> userNames = channelResponseDto.usersDto()
                .users()
                .stream()
                .map(UserDto::name)
                .toList();

        assertThat(userNames).contains(LOGIN_USER.getName(), OTHER_USER.getName());
    }

    @DisplayName("Public 채널 조회 시 User 모음에 null을 반환한다.")
    @Test
    void findByIdPublicChannel() {
        ChannelResponseDto channel = createPublicChannel(owner);
        ChannelResponseDto channelResponseDto = channelController.findById(channel.id());

        assertThat(channelResponseDto.usersDto()).isNull();
    }

    private ChannelResponseDto createPrivateChannel(UserDto owner) {
        return channelController.create(new ChannelRegisterDto(ChannelType.PRIVATE, CHANNEL_NAME, owner));
    }

    private ChannelResponseDto createPublicChannel(UserDto owner) {
        return channelController.create(new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME, owner));
    }
}