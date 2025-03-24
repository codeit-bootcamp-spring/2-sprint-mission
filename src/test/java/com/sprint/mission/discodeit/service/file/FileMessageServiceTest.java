package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.application.dto.message.MessageDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
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
    private FileUserRepository userRepository;
    private FileMessageRepository messageRepository;
    private MessageDto initializedMessage;
    private UserDto initializedUser;
    private Channel testChannel;

    @BeforeEach
    void setUp() {
        userTestFilePath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
        messageTestFilePath = STORAGE_DIRECTORY.resolve(MESSAGE_TEST_FILE);

        userRepository = new FileUserRepository();
        messageRepository = new FileMessageRepository();
        userRepository.changePath(userTestFilePath);
        messageRepository.changePath(messageTestFilePath);

        userService = new FileUserService(userRepository);
        messageService = new FileMessageService(messageRepository, userRepository);

        setUpUser();
        setUpChannel();
        setUpMessage();
    }

    private void setUpUser() {
        initializedUser = userService.register(
                new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    private void setUpChannel() {
        testChannel = new Channel(ChannelType.PUBLIC, "test");
    }

    private void setUpMessage() {
        initializedMessage = messageService.create(new MessageCreationDto(MESSAGE_CONTENT, testChannel.getId(), initializedUser.id()), new ArrayList<>());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(userTestFilePath);
        Files.deleteIfExists(messageTestFilePath);
    }

    @DisplayName("메시지를 생성하면 올바른 내용이 설정된다.")
    @Test
    void createMessage() {
        assertThat(initializedMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메시지 ID로 단건 조회 시 올바른 메시지를 반환한다.")
    @Test
    void findMessageById() {
        MessageDto message = messageService.findById(initializedMessage.messageId());
        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("같은 채널 ID를 가지는 메시지들을 조회하면 생성된 모든 메시지를 반환한다.")
    @Test
    void findMessagesByChannelId() {
        messageService.create(new MessageCreationDto(MESSAGE_CONTENT, testChannel.getId(), initializedUser.id()), new ArrayList<>());
        messageService.create(new MessageCreationDto(MESSAGE_CONTENT + "123", testChannel.getId(), initializedUser.id()), new ArrayList<>());

        List<MessageDto> messages = messageService.findAllByChannelId(testChannel.getId());

        assertThat(messages).hasSize(3); // 기존 메시지 포함 3개
    }

    @DisplayName("메시지 내용을 업데이트하면 변경된 내용이 반영된다.")
    @Test
    void updateMessageContext() {
        messageService.updateContext(initializedMessage.messageId(), MESSAGE_CONTENT + "123");
        assertThat(messageService.findById(initializedMessage.messageId()).context()).isEqualTo(MESSAGE_CONTENT + "123");
    }

    @DisplayName("메시지를 삭제하면 조회 시 예외가 발생한다.")
    @Test
    void deleteMessage() {
        UUID initId = initializedMessage.messageId();
        messageService.delete(initId);
        assertThatThrownBy(() -> messageService.findById(initId)).isInstanceOf(IllegalArgumentException.class);
    }
}