package com.sprint.mission.discodeit.service.fileTest;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FileMessageServiceTest {
    private FileMessageService messageService;
    private FileUserService userService;
    private FileChannelService channelService;
    private static final Logger logger = Logger.getLogger(FileMessageServiceTest.class.getName());

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        userService = FileUserService.getInstance();
        channelService = FileChannelService.getInstance(userService);
        messageService = FileMessageService.getInstance(userService, channelService);
    }

    @Test
    @DisplayName("FileMessageService: 여러 개의 메세지 추가 확인")
    void testMultipleMessagesOrder() {
        String userName = "user_" + UUID.randomUUID();
        userService.createUser(userName);
        User user = userService.getUserByName(userName);
        UUID userId = user.getId();

        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        messageService.createMessage(userId, channelId, "첫 번째 메세지");
        messageService.createMessage(userId, channelId, "두 번째 메세지");

        List<Message> messages = messageService.getChannelMessages(channelId);
        assertEquals(2, messages.size());
    }

    @Test
    @DisplayName("FileMessageService: 존재하지 않는 유저가 메세지 전송 시 예외 발생")
    void testMessageFromNonExistentUser() {
        UUID fakeUserId = UUID.randomUUID();
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.createMessage(fakeUserId, channelId, "유령 유저의 메세지");
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("FileMessageService: 존재하지 않는 채널에 메세지 전송 시 예외 발생")
    void testMessageToNonExistentChannel() {
        String userName = "user_" + UUID.randomUUID();
        userService.createUser(userName);
        User user = userService.getUserByName(userName);
        UUID userId = user.getId();
        UUID fakeChannelId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.createMessage(userId, fakeChannelId, "없는 채널에 메세지");
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("FileMessageService: 메세지 수정 확인")
    void testUpdateMessage() {
        String userName = "user_" + UUID.randomUUID();
        userService.createUser(userName);
        User user = userService.getUserByName(userName);
        UUID userId = user.getId();

        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        messageService.createMessage(userId, channelId, "원본 메세지");
        Message message = messageService.getChannelMessages(channelId).stream()
                .filter(m -> m.getContent().equals("원본 메세지"))
                .findFirst().get();
        UUID messageId = message.getId();

        messageService.updateMessage(messageId, "수정된 메세지");
        Message updatedMessage = messageService.getMessageById(messageId);
        assertEquals("수정된 메세지", updatedMessage.getContent());
    }

    @Test
    @DisplayName("FileMessageService: 존재하지 않는 메세지 수정 시 예외 발생")
    void testUpdateNonExistentMessage() {
        UUID fakeMessageId = UUID.randomUUID();

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            messageService.updateMessage(fakeMessageId, "수정된 메세지");
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 메세지입니다.", exception.getMessage());
    }
}
