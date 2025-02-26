package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class MessageServiceTest {
    private JCFUserService userService;
    private JCFChannelService channelService;
    private JCFMessageService messageService;

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance(userService);
        messageService = JCFMessageService.getInstance(userService, channelService);
    }

    @Test
    @DisplayName("메세지 생성 및 조회 확인")
    void testCreateAndRetrieveMessage() {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        userService.createUser("testUser");
        channelService.createChannel("testChannel");
        messageService.createMessage(userId, channelId, "Hello World");

        assertEquals("Hello World", messageService.getChannelMessages(channelId).get(0).getContent());
    }
}
