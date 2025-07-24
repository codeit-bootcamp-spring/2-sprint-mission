package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinaryContentEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishStatusChanged(String userId, BinaryContentDto dto) {
        try {
            BinaryContentStatusChangedEventPayload payload =
                    new BinaryContentStatusChangedEventPayload(userId, dto);
            String json = objectMapper.writeValueAsString(payload);

            kafkaTemplate.send("binary-content-status-topic", json);
            log.info("파일 상태 변경 이벤트 전송! userId={}, dto={}", userId, dto);
        } catch (Exception e) {
            log.error("Kafka 전송 실패", e);
        }
    }
}