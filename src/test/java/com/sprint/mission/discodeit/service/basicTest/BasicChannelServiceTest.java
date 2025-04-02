package com.sprint.mission.discodeit.service.basicTest;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.FileStorageManager;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // 모든 테스트 후 컨텍스트 리셋
class BasicChannelServiceTest {

    private static final String FILE_PATH = "src/main/resources/channels.dat";
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private FileChannelRepository channelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageManager fileStorageManager;
    private ConcurrentHashMap<UUID, Channel> originalData; // 원본 데이터 저장용

    private UUID testChannelId;
    private Channel testChannel;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        System.out.println("🔄 테스트 데이터 초기화 중...");

        // ✅ 기존 데이터 백업
        originalData = new ConcurrentHashMap<>(fileStorageManager.loadFile(FILE_PATH));

        testUserId = UUID.randomUUID();
        testChannelId = UUID.randomUUID();
        testChannel = new Channel("Test Channel");
        testChannel.addMembers(testUserId);

        // ✅ 테스트 데이터 추가
        channelRepository.addChannel(testChannel);
    }

    @AfterEach
    void tearDown() {
        System.out.println("♻ 테스트 후 데이터 롤백 중...");
        fileStorageManager.saveFile(FILE_PATH, originalData); // ✅ 원본 데이터로 복원
    }

    /**
     * =======================
     * BasicChannelService 테스트
     * =======================
     */

    @Test
    @DisplayName("BasicChannelService: 채널 생성 확인")
    void testCreateChannel() {
        String channelName = "newChannel_" + UUID.randomUUID();
        Channel createdChannel = basicChannelService.createChannel(channelName);

        assertNotNull(createdChannel);
        assertEquals(channelName, createdChannel.getChannelName());

        List<Channel> channels = channelRepository.findAllChannels();
        assertTrue(channels.contains(createdChannel));
    }

    @Test
    @DisplayName("BasicChannelService: 존재하는 채널 조회")
    void testFindChannelById() {
        Channel foundChannel = basicChannelService.findChannelById(testChannelId);
        assertNotNull(foundChannel);
        assertEquals(testChannel.getChannelName(), foundChannel.getChannelName());
    }

    @Test
    @DisplayName("BasicChannelService: 존재하지 않는 채널 조회 시 예외 발생")
    void testFindNonExistentChannel() {
        UUID fakeChannelId = UUID.randomUUID();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            basicChannelService.findChannelById(fakeChannelId);
        });

        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("BasicChannelService: 채널 이름 변경")
    void testUpdateChannelName() {
        String newChannelName = "Updated Channel Name";

        basicChannelService.updateChannelName(testChannelId, newChannelName);

        Channel updatedChannel = basicChannelService.findChannelById(testChannelId);
        assertEquals(newChannelName, updatedChannel.getChannelName());
    }

    @Test
    @DisplayName("BasicChannelService: 채널에 유저 추가")
    void testAddUserToChannel() {
        basicChannelService.addUser(testChannelId, testUserId);

        Channel updatedChannel = basicChannelService.findChannelById(testChannelId);
        assertTrue(updatedChannel.getMembers().contains(testUserId));
    }

    @Test
    @DisplayName("BasicChannelService: 채널에서 유저 삭제")
    void testRemoveUserFromChannel() {
        basicChannelService.removeUser(testChannelId, testUserId);

        Channel updatedChannel = basicChannelService.findChannelById(testChannelId);
        assertFalse(updatedChannel.getMembers().contains(testUserId));
    }

    @Test
    @DisplayName("BasicChannelService: 채널 삭제")
    void testDeleteChannel() {
        basicChannelService.deleteChannel(testChannelId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            basicChannelService.findChannelById(testChannelId);
        });

        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("BasicChannelService: 채널에 메세지 추가")
    void testAddMessageToChannel() {
        UUID messageId = UUID.randomUUID();

        basicChannelService.addMessage(testChannelId, messageId);

        Channel updatedChannel = basicChannelService.findChannelById(testChannelId);
        assertTrue(updatedChannel.getMessages().contains(messageId));
    }

    @Test
    @DisplayName("BasicChannelService: 채널에서 메세지 삭제")
    void testRemoveMessageFromChannel() {
        UUID messageId = UUID.randomUUID();
        testChannel.addMessages(messageId);
        basicChannelService.removeMessage(testChannelId, messageId);

        Channel updatedChannel = basicChannelService.findChannelById(testChannelId);
        assertFalse(updatedChannel.getMessages().contains(messageId));
    }

    @Test
    @DisplayName("BasicChannelService: 채널이 존재하는지 확인")
    void testValidateChannelExists() {
        assertDoesNotThrow(() -> basicChannelService.validateChannelExists(testChannelId));
    }

    @Test
    @DisplayName("BasicChannelService: 존재하지 않는 채널 확인 시 예외 발생")
    void testValidateNonExistentChannel() {
        UUID fakeChannelId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            basicChannelService.validateChannelExists(fakeChannelId);
        });

        assertEquals("존재하지 않는 채널입니다.", exception.getMessage());
    }
}
