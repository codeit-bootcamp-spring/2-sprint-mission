package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserRegisterDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.*;
import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileMessageServiceTest {
    private Path userTestFilePath;
    private Path messageTestFilePath;
    private UserService userService;
    private MessageService messageService;
    private MessageDto initializedMessage;
    private UserDto initializedUser;

    @BeforeEach
    void setUp() {
        setUpTestFilePath();
        setUpService();
        setUpUser();
        setUpMessage();
    }

    private void setUpTestFilePath() {
        userTestFilePath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
        messageTestFilePath = STORAGE_DIRECTORY.resolve(MESSAGE_TEST_FILE);
    }

    private void setUpService() {
        FileUserRepository userRepository = new FileUserRepository();
        userRepository.changePath(userTestFilePath);
        FileMessageRepository messageRepository = new FileMessageRepository();
        messageRepository.changePath(messageTestFilePath);

        userService = new FileUserService(userRepository);
        messageService = new FileMessageService(messageRepository, userRepository);
    }

    private void setUpMessage() {
        initializedMessage = messageService.create(new MessageCreationDto(MESSAGE_CONTENT, UUID.randomUUID(), initializedUser.id()), new ArrayList<>());
    }

    private void setUpUser() {
        initializedUser = userService.register(
                new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @AfterEach
    void tearDown() throws IOException {
        initializeFiles();
    }

    private void initializeFiles() throws IOException {
        Files.deleteIfExists(userTestFilePath);
        Files.deleteIfExists(messageTestFilePath);
    }

    @DisplayName("메세지 생성")
    @Test
    void create() {
        assertThat(initializedMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("단일 메세지ID로 조회")
    @Test
    void findById() {
        MessageDto message = messageService.findById(initializedMessage.messageId());

        assertThat(message.context())
                .isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("같은 채널ID를 가지는 메세지들을 조회합니다")
    @Test
    void findByChannelId() {
        Channel channel = new Channel(ChannelType.PUBLIC, "test");
        messageService.create(new MessageCreationDto(MESSAGE_CONTENT, channel.getId(), initializedUser.id()), new ArrayList<>());
        messageService.create(new MessageCreationDto(MESSAGE_CONTENT + "123", channel.getId(), initializedUser.id()), new ArrayList<>());

        List<MessageDto> messages = messageService.findByChannelId(channel.getId());

        assertThat(messages).hasSize(2);
    }

    @DisplayName("메세지의 내용을 업데이트 합니다")
    @Test
    void updateContext() {
        messageService.updateContext(initializedMessage.messageId(), MESSAGE_CONTENT + "123");

        assertThat(messageService.findById(initializedMessage.messageId()).context())
                .isNotEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메세지를 삭제합니다")
    @Test
    void delete() {
        UUID initId = initializedMessage.messageId();
        messageService.delete(initId);

        assertThatThrownBy(() -> messageService.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}