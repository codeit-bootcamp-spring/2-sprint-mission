package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishUsersRefresh(UUID userId) {
        try{
            String json = objectMapper.writeValueAsString(Map.of("userId", userId));
            kafkaTemplate.send("users-refresh-topic", json);
            log.info("사용자 목록 갱신 이벤트 전송 userId={}", userId);
        } catch (Exception e) {
            log.error("Kafka 전송 실패", e);
        }
    }
}