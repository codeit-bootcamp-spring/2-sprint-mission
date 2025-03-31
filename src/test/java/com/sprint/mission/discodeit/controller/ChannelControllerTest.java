package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
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
    private UserResult setUpUser;

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

        setUpUser = userService.register(new UserRequest(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @DisplayName("Public 채널 생성")
    @Test
    void createPublicChannel() {
        ChannelResult channel = channelController.createPublicChannel(new ChannelCreateRequest(ChannelType.PUBLIC, "Public", setUpUser.id()));

        assertThat(channel.type()).isEqualTo(ChannelType.PUBLIC);
    }

    @DisplayName("Private 채널 생성시 채널 멤버의 ID를 반환한다.")
    @Test
    void createPrivateChannel() {
        UserResult otherUser = userService.register(new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        ChannelResult privateChannel = createPrivateChannel(setUpUser, List.of(otherUser.id()));

        assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(otherUser.id());
    }

    @DisplayName("Private 채널 조회 시 유저에게만 속한 Private 채널을 반환한다.")
    @Test
    void findAll() {
        ChannelResult privateChannel = createPrivateChannel(setUpUser, new ArrayList<>());
        ChannelResult userPublicChannel = channelController.createPublicChannel(new ChannelCreateRequest(ChannelType.PUBLIC, "Public", setUpUser.id()));
        UserResult otherUser = userService.register(new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        createPrivateChannel(otherUser, new ArrayList<>());


        List<UUID> channelIds = channelController.getAllByUserId(setUpUser.id()).stream().map(ChannelResult::id).toList();

        assertThat(channelIds).containsExactlyInAnyOrder(privateChannel.id(), userPublicChannel.id());
    }

    @DisplayName("Private채널에 맴버를 추가한다")
    @Test
    void findByIdPrivateChannel_MemberTogether() {
        UserResult otherUser = userService.register(new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword()), null);
        ChannelResult channel = createPrivateChannel(setUpUser, new ArrayList<>());

        ChannelResult privateChannel = channelController.addPrivateChannelMember(channel.id(), OTHER_USER.getEmail());

        assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(setUpUser.id(), otherUser.id());
    }

    private ChannelResult createPrivateChannel(UserResult loginUser, List<UUID> memberIds) {
        return channelController.createPrivateChannel(new ChannelCreateRequest(ChannelType.PRIVATE, CHANNEL_NAME, loginUser.id()), memberIds);
    }
}