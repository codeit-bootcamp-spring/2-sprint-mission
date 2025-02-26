package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.logging.Logger;

class JCFUserServiceTest {
    private JCFUserService userService;
    private JCFChannelService channelService;
    private static final Logger logger = Logger.getLogger(JCFUserServiceTest.class.getName());

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance(userService);
    }

    @Test
    @DisplayName("JCFUserService: 정상적인 유저 생성 확인")
    void testCreateValidUser() {
        String uniqueUsername = "user_" + UUID.randomUUID();
        userService.createUser(uniqueUsername);
        User createdUser = userService.getUserByName(uniqueUsername);
        assertNotNull(createdUser);
        assertEquals(uniqueUsername, createdUser.getUsername());
        logger.info("유저 생성 확인: " + createdUser.getUsername());
    }

    @Test
    @DisplayName("JCFUserService: 중복된 유저 생성 시 예외 발생")
    void testCreateDuplicateUser() {
        String uniqueUsername = "user_" + UUID.randomUUID();
        userService.createUser(uniqueUsername);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(uniqueUsername);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("이미 존재하는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFUserService: 모든 유저 리스트 가져오기 확인")
    void testGetAllUsers() {
        userService.createUser("user_" + UUID.randomUUID());
        userService.createUser("user_" + UUID.randomUUID());
        assertTrue(userService.getAllUsers().size() >= 2);
        logger.info("총 유저 수: " + userService.getAllUsers().size());
    }

    @Test
    @DisplayName("JCFUserService: 유저 삭제 후 조회 시 예외 발생 확인")
    void testDeleteUserAndCheck() {
        String uniqueUsername = "user_" + UUID.randomUUID();
        userService.createUser(uniqueUsername);
        User user = userService.getUserByName(uniqueUsername);
        UUID userId = user.getId();
        userService.deleteUser(userId);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(userId);
        });
        logger.info("유저 삭제 후 조회 예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("JCFUserService: 채널에 유저 추가 후 정상적으로 포함되는지 확인")
    void testAddUserToChannelAndCheck() {
        userService.createUser("testUser_" + UUID.randomUUID());
        User user = userService.getUserByName(userService.getAllUsers().get(0).getUsername());
        UUID userId = user.getId();
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().orElseThrow(() -> new IllegalStateException("채널을 찾을 수 없습니다."))
                .getId();
        channelService.addUserToChannel(channelId, userId);
        assertTrue(channelService.getChannelById(channelId).getMembers().contains(userId));
        logger.info("채널 내 유저 추가 확인: " + userId);
    }

    @Test
    @DisplayName("JCFUserService: 채널에 없는 유저 삭제 시 예외 발생")
    void testRemoveUserNotInChannel() {
        userService.createUser("testUser_" + UUID.randomUUID());
        User user = userService.getUserByName(userService.getAllUsers().get(0).getUsername());
        UUID userId = user.getId();
        String channelName = "testChannel_" + UUID.randomUUID();
        channelService.createChannel(channelName);
        UUID channelId = channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().orElseThrow(() -> new IllegalStateException("채널을 찾을 수 없습니다."))
                .getId();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            channelService.removeUserFromChannel(channelId, userId);
        });
        logger.info("채널에 없는 유저 삭제 예외 확인: " + exception.getMessage());
        assertEquals("채널에 존재하지 않는 유저입니다.", exception.getMessage());
    }
}
