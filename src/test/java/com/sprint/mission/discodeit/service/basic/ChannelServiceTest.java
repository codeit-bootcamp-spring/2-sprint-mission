package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;


@SpringBootTest
class ChannelServiceTest {

    @Autowired
    ChannelService channelService;

    @Test
    void testCacheEffect() {
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // 캐시 적용 전
        StopWatch stopwatch1 = new StopWatch();
        stopwatch1.start();
        channelService.findAllByUserId(userId);
        stopwatch1.stop();

        // 캐시 적용 후
        StopWatch stopwatch2 = new StopWatch();
        stopwatch2.start();
        channelService.findAllByUserId(userId);
        stopwatch2.stop();

        System.out.println("🚫 캐시 미적용 시간: " + stopwatch1.getTotalTimeMillis() + "ms");
        System.out.println("✅ 캐시 적용 후 시간: " + stopwatch2.getTotalTimeMillis() + "ms");
    }
}