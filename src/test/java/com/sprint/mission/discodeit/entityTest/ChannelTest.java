package com.sprint.mission.discodeit.entityTest;

import com.sprint.mission.discodeit.entity.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class ChannelTest {
    private Channel channel;

    @BeforeEach
    void setUp() {
        channel = new Channel("testChannel");
    }

    @Test
    @DisplayName("채널 생성 확인")
    void testChannelCreation() {
        assertNotNull(channel.getId());
        assertEquals("testChannel", channel.getChannelName());
    }

    @Test
    @DisplayName("멤버 추가 및 삭제 확인")
    void testMemberManagement() {
        UUID userId = UUID.randomUUID();
        channel.addMembers(userId);
        assertTrue(channel.isUserInChannel(userId));

        channel.removeMember(userId);
        assertFalse(channel.isUserInChannel(userId));
    }
}