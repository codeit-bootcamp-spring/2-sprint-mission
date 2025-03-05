package com.sprint.mission.discodeit.service.jcfTest;

import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.List;
import java.util.logging.Logger;

class JCFMessageServiceTest {
    private JCFMessageService messageService;
    private JCFUserService userService;
    private JCFChannelService channelService;
    private static final Logger logger = Logger.getLogger(JCFMessageServiceTest.class.getName());

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance(userService);
        messageService = JCFMessageService.getInstance(userService, channelService);
    }

    @Test
    @DisplayName("JCFMessageService: 여러 개의 메세지 추가 확인")
    void testMultipleMessagesOrder() {
        userService.createUser("testUser_" + UUID.randomUUID());
        User user = userService.getUserByName(userService.getAllUsers().get(0).getUsername());
        UUID userId = user.getId();

        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        System.out.println(channelId);

        messageService.createMessage(userId, channelId, "첫 번째 메세지");
        messageService.createMessage(userId, channelId, "두 번째 메세지");

        List<Message> messages = messageService.getChannelMessages(channelId);
        assertEquals(2, messages.size());
    }

    @Test
    @DisplayName("JCFMessageService: 존재하지 않는 유저가 메세지 전송 시 예외 발생")
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
    @DisplayName("JCFMessageService: 존재하지 않는 채널에 메세지 전송 시 예외 발생")
    void testMessageToNonExistentChannel() {
        userService.createUser("testUser_" + UUID.randomUUID());
        User user = userService.getUserByName(userService.getAllUsers().get(0).getUsername());
        UUID userId = user.getId();
        UUID fakeChannelId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.createMessage(userId, fakeChannelId, "없는 채널에 메세지");
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFMessageService: 메세지 수정 확인")
    void testUpdateMessage() {
        userService.createUser("testUser_" + UUID.randomUUID());
        User user = userService.getUserByName(userService.getAllUsers().get(0).getUsername());
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
    @DisplayName("JCFMessageService: 존재하지 않는 메세지 수정 시 예외 발생")
    void testUpdateNonExistentMessage() {
        UUID fakeMessageId = UUID.randomUUID();

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            messageService.updateMessage(fakeMessageId, "수정된 메세지");
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 메세지입니다.", exception.getMessage());
    }
}
