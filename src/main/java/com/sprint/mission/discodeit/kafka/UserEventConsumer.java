package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.SseService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final ObjectMapper objectMapper;
    private final SseService sseService;

    @KafkaListener(topics = "users-refresh-topic", groupId = "users-refresh-group")
    public void consume(String message) {
        try{
            // payload 간단하니까 바로 처리
            Map<String, Object> event = objectMapper.readValue(message, new TypeReference<>() {});
            sseService.sendEvent(
                    "ALL", // 전체 유저에게 전송
                    "users.refresh",
                    event // {userId: ...}
            );
            log.info("사용자 목록 갱신 SSE push 완료 userId={}", event.get("userId"));
        } catch (Exception e) {
            log.error("Kafka 알림 메시지 처리 실패 - payload={}", message, e);
        }
    }
}