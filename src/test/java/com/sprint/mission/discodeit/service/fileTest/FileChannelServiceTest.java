package com.sprint.mission.discodeit.service.fileTest;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FileChannelServiceTest {
    private static final Logger logger = Logger.getLogger(FileChannelServiceTest.class.getName());
    private FileChannelService fileChannelService;
    private FileUserService userService;

//    @BeforeEach
//    void setUp(@TempDir Path tempDir) {
//        userService = FileUserService.getInstance();
//        fileChannelService = FileChannelService.getInstance(userService);
//    }

    @Test
    @DisplayName("FileChannelService: 채널 생성 확인")
    void testCreateChannel() {
        String channelName = "testChannel_" + UUID.randomUUID();
        fileChannelService.createChannel(channelName);

        assertNotNull(fileChannelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst()
                .orElse(null));
    }

    @Test
    @DisplayName("FileChannelService: 존재하지 않는 채널 조회 시 예외 확인")
    void testChannelNotFound() {
        UUID randomId = UUID.randomUUID();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileChannelService.findChannelById(randomId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("FileChannelService: 채널에 유저 추가 및 제거 확인")
    void testAddAndRemoveUser() {
        String channelName = "testChannel_" + UUID.randomUUID();
        fileChannelService.createChannel(channelName);
        UUID channelId = fileChannelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        User user = userService.createUser("user_" + UUID.randomUUID());

        UUID userId = user.getId();

        fileChannelService.addUser(channelId, userId);
        assertTrue(fileChannelService.findChannelById(channelId).getMembers().contains(userId));

        fileChannelService.removeUser(channelId, userId);
        assertFalse(fileChannelService.findChannelById(channelId).getMembers().contains(userId));
    }

    @Test
    @DisplayName("FileChannelService: 채널 삭제 확인")
    void testDeleteChannel() {
        String channelName = "testChannel_" + UUID.randomUUID();
        fileChannelService.createChannel(channelName);
        UUID channelId = fileChannelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst().get().getId();

        fileChannelService.deleteChannel(channelId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileChannelService.findChannelById(channelId);
        });
        logger.info("예외 발생 확인: " + exception.getMessage());
        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }
}
