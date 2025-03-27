package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.application.dto.message.MessageDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DiscodeitApplicationTest {
    @Autowired
    private UserController userController;
    @Autowired
    private ChannelController channelController;
    @Autowired
    private MessageController messageController;
    private UserDto setUpUser;
    private ChannelDto setUpChannel;

    @BeforeEach
    void setUp() {
        setUpUser = setupUser();
        setUpChannel = setupChannel();
    }

    @DisplayName("Public 메세지 생성 테스트")
    @Test
    void createMessage() {
        MessageCreationDto messageCreationDto = new MessageCreationDto(MESSAGE_CONTENT, setUpChannel.id(), setUpUser.id());
        MessageDto message = messageController.createMessage(messageCreationDto, new ArrayList<>());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    private UserDto setupUser() {
        UserRegisterDto userRegisterDto = new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword());
        return userController.register(userRegisterDto, null);
    }

    private ChannelDto setupChannel() {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, "7팀", setUpUser.id());
        return channelController.createPublicChannel(channelRegisterDto);
    }
}