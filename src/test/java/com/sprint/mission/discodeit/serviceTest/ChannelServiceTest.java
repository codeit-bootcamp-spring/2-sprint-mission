package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChannelServiceTest {
    private JCFUserService userService;
    private JCFChannelService channelService;

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance(userService);
    }

    @Test
    @DisplayName("채널 생성 확인")
    void testCreateChannel() {
        channelService.createChannel("testChannel");
        assertNotNull(channelService.getAllChannels().stream()
                .filter(c -> c.getChannelName().equals("testChannel"))
                .findFirst()
                .orElse(null));
    }
}
