package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
        setUpUser = userController.register(new UserRegisterDto("황지환", "황지환@naver.com", "1234"));
        setUpChannel = channelController.create("11", setUpUser);
    }

    @DisplayName("채널에 메세지 생성 테스트")
    @Test
    void 메세지_생성_테스트() {
        String context = "안녕하세요";
        MessageDto message = messageController.createMessage(context, setUpChannel.id(), setUpUser.id());

        assertThat(message.context()).isEqualTo(context);
    }
}