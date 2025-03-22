package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.application.user.UserRegisterDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

class ChannelControllerTest {
    private ChannelController channelController;
    private UserService userService;
    private MessageService messageService;
    private UserDto owner;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        MessageRepository messageRepository = new JCFMessageRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        userService = new BasicUserService(userRepository, userStatusRepository);
        messageService = new BasicMessageService(messageRepository, userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository, readStatusRepository);
        channelController = new ChannelController(channelService, messageService, userService, new BasicReadStatusService(readStatusRepository));

        UserRegisterDto userRegisterDto = new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword());
        owner = userService.register(userRegisterDto, null);
    }

    @DisplayName("채널조회시 가장 최근 메세지의 시간정보를 반환합니다.")
    @Test
    void findById() {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME, owner);
        ChannelResponseDto channel = channelController.create(channelRegisterDto);

        messageService.create(MESSAGE_CONTENT, channel.id(), owner.id());
        MessageDto messageDto = messageService.create(MESSAGE_CONTENT + 123, channel.id(), owner.id());
        MessageDto message = messageService.findById(messageDto.messageId());


        ChannelResponseDto channelRe = channelController.findById(channel.id());
        assertThat(channelRe.lastMessageCreatedAt()).isEqualTo(message.createdAt());
    }

    @DisplayName("Private 채널 조회시 유저의 세부 정보를 반환합니다.")
    @Test
    void findByIdPrivateChannel() {
        UserRegisterDto userRegisterDto2 = new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword());
        userService.register(userRegisterDto2, null);
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PRIVATE, CHANNEL_NAME, owner);
        ChannelResponseDto channel = channelController.create(channelRegisterDto);

        ChannelResponseDto channelResponseDto = channelController.addMemberToPrivate(channel.id(), OTHER_USER.getEmail());
        List<String> userNames = channelResponseDto.usersDto()
                .users()
                .stream()
                .map(UserDto::name)
                .toList();

        assertThat(userNames).contains(LOGIN_USER.getName(), OTHER_USER.getName());
    }


    @DisplayName("public 채널 조회시 User모음에 null을 반환합니다.")
    @Test
    void findByIdPublicChannel() {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME, owner);
        ChannelResponseDto channel = channelController.create(channelRegisterDto);
        ChannelResponseDto channelResponseDto = channelController.findById(channel.id());

        assertThat(channelResponseDto.usersDto()).isNull();
    }
}