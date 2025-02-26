package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.logging.Logger;

class JCFChannelServiceTest {
    private JCFChannelService channelService;
    private JCFUserService userService;
    private static final Logger logger = Logger.getLogger(JCFChannelServiceTest.class.getName());

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance(userService);
    }

    @Test
    @DisplayName("JCFChannelService: 채널 생성 확인")
    void testCreateChannel() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);

        assertNotNull(channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst()
                .orElse(null));
    }

    @Test
    @DisplayName("JCFChannelService: 존재하지 않는 채널 조회 시 예외 확인")
    void testChannelNotFound() {
        UUID randomId = UUID.randomUUID();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            channelService.getChannelById(randomId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFChannelService: 이미 존재하는 채널 생성 시 예외 확인")
    void testDuplicateChannelCreation() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            channelService.createChannel(channelName);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("이미 존재하는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFChannelService: 채널에 유저 추가 및 제거 확인")
    void testAddAndRemoveUser() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        userService.createUser("testUser");
        User user = userService.getUserByName("testUser");
        UUID userId = user.getId();

        channelService.addUserToChannel(channelId, userId);
        assertTrue(channelService.getChannelById(channelId).getMembers().contains(userId));

        channelService.removeUserFromChannel(channelId, userId);
        assertFalse(channelService.getChannelById(channelId).getMembers().contains(userId));
    }

    @Test
    @DisplayName("JCFChannelService: 채널 삭제 확인")
    void testRemoveChannel() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        channelService.removeChannel(channelId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            channelService.getChannelById(channelId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }
}