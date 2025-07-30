package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.BinaryContentStatusChangedEventPayload;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentStatusChangedConsumer {

    private final ObjectMapper objectMapper;
    private final SseService sseService;

    @KafkaListener(
            topics = "binary-content-status-topic",
            groupId = "binary-content-status-group"
    )
    public void consume(String message) {
        try {
            BinaryContentStatusChangedEventPayload event = objectMapper.readValue(
                    message, BinaryContentStatusChangedEventPayload.class
            );

            // SSE 전송
            sseService.sendEvent(
                    event.userId(),
                    "binaryContents.status",
                    event.binaryContentDto()
            );
            log.info("파일 업로드 상태 변경 SSE push 완료. userId = {}", event.userId());
        } catch (Exception e) {
            log.error("Kafka 파일 상태 변경 메시지 처리 실패", e);
        }
    }

}