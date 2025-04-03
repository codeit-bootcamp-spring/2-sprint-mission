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
class DiscodeitApplicationTest {
}