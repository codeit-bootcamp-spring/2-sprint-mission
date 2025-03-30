package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
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
    private UserResult setUpUser;
    private ChannelRequest setUpChannel;

    @BeforeEach
    void setUp() {
        setUpUser = setupUser();
        setUpChannel = setupChannel();
    }

    @DisplayName("Public 메세지 생성 테스트")
    @Test
    void createMessage() {
        MessageCreationRequest messageCreationRequest = new MessageCreationRequest(MESSAGE_CONTENT, setUpChannel.id(), setUpUser.id());
        MessageResult message = messageController.createMessage(messageCreationRequest, new ArrayList<>());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    private UserResult setupUser() {
        UserRequest userRequest = new UserRequest(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword());
        return userController.register(userRequest, null).getBody();
    }

    private ChannelRequest setupChannel() {
        ChannelRegisterRequest channelRegisterRequest = new ChannelRegisterRequest(ChannelType.PUBLIC, "7팀", setUpUser.id());
        return channelController.createPublicChannel(channelRegisterRequest);
    }
}