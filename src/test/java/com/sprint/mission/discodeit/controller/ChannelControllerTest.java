package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private UserDto setUpUser;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        messageRepository = new JCFMessageRepository();
        channelRepository = new JCFChannelRepository();
        readStatusRepository = new JCFReadStatusRepository();

        userService = new BasicUserService(userRepository, userStatusRepository);
        channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
        channelController = new ChannelController(channelService, userService);

        setUpUser = userService.register(new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @DisplayName("Public 채널 생성")
    @Test
    void createPublicChannel() {
        ChannelDto channel = channelController.createPublicChannel(new ChannelRegisterDto(ChannelType.PUBLIC, "Public", setUpUser.id()));

        assertThat(channel.type()).isEqualTo(ChannelType.PUBLIC);
    }

    @DisplayName("Private 채널 생성시 채널 멤버의 ID를 반환한다.")
    @Test
    void createPrivateChannel() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        PrivateChannelDto privateChannel = (PrivateChannelDto) createPrivateChannel(setUpUser, List.of(otherUser.id()));

        assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(otherUser.id());
    }

    @DisplayName("Private 채널 조회 시 유저에게만 속한 Private 채널을 반환한다.")
    @Test
    void findAll() {
        PrivateChannelDto userPrivateChannel = (PrivateChannelDto) createPrivateChannel(setUpUser, new ArrayList<>());
        ChannelDto userPublicChannel = channelController.createPublicChannel(new ChannelRegisterDto(ChannelType.PUBLIC, "Public", setUpUser.id()));
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        createPrivateChannel(otherUser, new ArrayList<>());


        List<UUID> channelIds = channelController.findAllByUserId(setUpUser.id()).stream().map(ChannelDto::id).toList();

        assertThat(channelIds).containsExactlyInAnyOrder(userPrivateChannel.id(), userPublicChannel.id());
    }

    @DisplayName("Private채널에 맴퍼를 추가한다")
    @Test
    void findByIdPrivateChannel_MemberTogether() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        ChannelDto channel = createPrivateChannel(setUpUser, new ArrayList<>());

        PrivateChannelDto privateChannel = (PrivateChannelDto) channelController.addPrivateChannelMember(channel.id(), OTHER_USER.getEmail());

        assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(setUpUser.id(), otherUser.id());
    }

    private ChannelDto createPrivateChannel(UserDto loginUser, List<UUID> memberIds) {
        return channelController.createPrivateChannel(new ChannelRegisterDto(ChannelType.PRIVATE, CHANNEL_NAME, loginUser.id()), memberIds);
    }
}