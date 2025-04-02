package com.sprint.mission.discodeit.service.jcfTest;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class JCFChannelServiceTest {
    private static final Logger logger = Logger.getLogger(JCFChannelServiceTest.class.getName());
    private JCFChannelService channelService;
    private JCFUserService userService;

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
            channelService.findChannelById(randomId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFChannelService: 채널에 유저 추가 및 제거 확인")
    void testAddAndRemoveUser() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        User user = userService.createUser("testUser");
        UUID userId = user.getId();

        channelService.addUser(channelId, userId);
        assertTrue(channelService.findChannelById(channelId).getMembers().contains(userId));

        channelService.removeUser(channelId, userId);
        assertFalse(channelService.findChannelById(channelId).getMembers().contains(userId));
    }

    @Test
    @DisplayName("JCFChannelService: 채널 삭제 확인")
    void testDeleteChannel() {
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        channelService.deleteChannel(channelId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            channelService.findChannelById(channelId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }
}