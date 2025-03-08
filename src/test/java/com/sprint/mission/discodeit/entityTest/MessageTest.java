package com.sprint.mission.discodeit.entityTest;

import com.sprint.mission.discodeit.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class MessageTest {
    private Message message;

    @BeforeEach
    void setUp() {
        UUID senderId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        message = new Message(senderId, channelId, "Hello World");
    }

    @Test
    @DisplayName("메세지 생성 확인")
    void testMessageCreation() {
        assertNotNull(message.getId());
        assertEquals("Hello World", message.getContent());
    }

    @Test
    @DisplayName("메세지 수정 확인")
    void testMessageUpdate() {
        message.updateContent("Updated Message");
        assertEquals("Updated Message", message.getContent());
    }
}
