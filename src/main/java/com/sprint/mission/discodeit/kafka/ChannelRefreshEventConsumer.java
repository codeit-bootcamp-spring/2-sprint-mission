package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.ChannelRefreshEventPayload;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChannelRefreshEventConsumer {
    private final ObjectMapper objectMapper;
    private final SseService sseService;
    private final CacheManager cacheManager;

    @KafkaListener(topics = "channel-refresh-topic", groupId = "channel-refresh-group")
    public void consume(String message) {
        try{
            ChannelRefreshEventPayload event = objectMapper.readValue(message, ChannelRefreshEventPayload.class);

            // 캐시 무효화
            for (String userId : event.userIds()) {
                Objects.requireNonNull(cacheManager.getCache("userChannels")).evict(userId);
            }
            Objects.requireNonNull(cacheManager.getCache("findAllUsers")).clear();

            // sse send
            for (String userId : event.userIds()) {
                sseService.sendEvent(
                        userId,
                        "channels.refresh",
                        Map.of("channelId", event.channelId())
                );
            }
        } catch (Exception e) {
            log.error("Kafka 채널 목록 갱신 이벤트 처리 실패", e);
        }
    }

}