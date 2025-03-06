package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.config.FilePath.MESSAGE_FILE;
import static com.sprint.mission.config.FilePath.USER_FILE;
import static com.sprint.mission.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.constants.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileMessageServiceTest {
    private final UserService userService = new FileUserService();
    private final MessageService messageService = new FileMessageService(new FileUserService());
    private MessageDto setUpMessage;
    private UserDto loginUser;

    @BeforeEach
    void setUp() {
        loginUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()));

        setUpMessage = messageService.create(MESSAGE_CONTENT, UUID.randomUUID(), loginUser.id());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(MESSAGE_FILE.getPath());
        Files.deleteIfExists(USER_FILE.getPath());
    }

    @DisplayName("메세지 생성")
    @Test
    void create() {
        assertThat(setUpMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("단일 메세지ID로 조회")
    @Test
    void findById() {
        MessageDto message = messageService.findById(setUpMessage.messageId());

        assertThat(message.context())
                .isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("같은 채널ID를 가지는 메세지들을 조회합니다")
    @Test
    void findByChannelId() {
        Channel channel = new Channel("test", UUID.randomUUID());
        messageService.create(MESSAGE_CONTENT, channel.getId(), loginUser.id());
        messageService.create(MESSAGE_CONTENT + "123", channel.getId(), loginUser.id());

        List<MessageDto> messages = messageService.findByChannelId(channel.getId());

        assertThat(messages).hasSize(2);
    }

    @DisplayName("메세지의 내용을 업데이트 합니다")
    @Test
    void updateContext() {
        messageService.updateContext(setUpMessage.messageId(), MESSAGE_CONTENT + "123");

        assertThat(messageService.findById(setUpMessage.messageId()).context())
                .isNotEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메세지를 삭제합니다")
    @Test
    void delete() {
        UUID initId = setUpMessage.messageId();
        messageService.delete(initId);

        assertThatThrownBy(() -> messageService.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}