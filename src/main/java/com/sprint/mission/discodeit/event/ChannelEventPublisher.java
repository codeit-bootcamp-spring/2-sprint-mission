package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishRefresh(List<String> userIds, UUID channelId) {
        try {
            ChannelRefreshEventPayload payload = new ChannelRefreshEventPayload(userIds, channelId);
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("channel-refresh-topic", json);
            log.info("채널 목록 갱신 이벤트 전송 userCount={}, channelId={}", userIds.size(), channelId);
        } catch (Exception e) {
            log.error("Kafka 채널 목록 갱신 전송 실패", e);
        }
    }

}
