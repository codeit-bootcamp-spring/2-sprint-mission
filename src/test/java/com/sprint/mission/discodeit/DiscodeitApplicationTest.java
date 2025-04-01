package com.sprint.mission.discodeit;

import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
  private ChannelResult setUpChannel;

  @BeforeEach
  void setUp() {
    setUpUser = setupUser();
    setUpChannel = setupChannel();
  }

  @DisplayName("Public 메세지 생성 테스트")
  @Test
  void createMessage() {
    MessageCreationRequest messageCreationRequest = new MessageCreationRequest(MESSAGE_CONTENT,
        setUpChannel.id(), setUpUser.id());
    MessageResult message = messageController.createMessage(messageCreationRequest,
        new ArrayList<>()).getBody();

    assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
  }

  private UserResult setupUser() {
    UserCreateRequest userRequest = new UserCreateRequest(LOGIN_USER.getName(),
        LOGIN_USER.getEmail(), LOGIN_USER.getPassword());
    return userController.register(userRequest, null).getBody();
  }

  private ChannelResult setupChannel() {
    ChannelCreateRequest channelRegisterRequest = new ChannelCreateRequest("7팀", setUpUser.id());
    return channelController.createPublicChannel(channelRegisterRequest).getBody();
  }

  @AfterEach
  void cleanupFiles() throws Exception {
    List<String> filePaths = List.of(
        "filestorage/test/binary-content.ser",
        "filestorage/test/channel.ser",
        "filestorage/test/user.ser",
        "filestorage/test/userStatus.ser",
        "filestorage/test/message.ser");

    for (String filePath : filePaths) {
      Path path = Path.of(filePath);
      Files.deleteIfExists(path);
    }
  }
}