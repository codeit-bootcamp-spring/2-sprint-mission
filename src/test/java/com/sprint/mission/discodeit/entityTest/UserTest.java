package com.sprint.mission.discodeit.entityTest;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser");
    }

    @Test
    @DisplayName("유저 생성 확인")
    void testUserCreation() {
        assertNotNull(user.getId());
        assertEquals("testUser", user.getUsername());
    }

    @Test
    @DisplayName("채널 추가 및 삭제 확인")
    void testChannelManagement() {
        UUID channelId = UUID.randomUUID();
        user.addJoinedChannel(channelId);
        assertTrue(user.isJoinedChannel(channelId));

        user.removeJoinedChannel(channelId);
        assertFalse(user.isJoinedChannel(channelId));
    }
}
