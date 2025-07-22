package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.KafkaPublishableEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(KafkaPublishableEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String topic = event.topic();
            kafkaTemplate.send(topic, payload)
                    .thenAccept(result ->
                            log.info("Kafka 전송 성공 - topic={}, offset={}", topic, result.getRecordMetadata().offset())
                    )
                    .exceptionally(ex -> {
                        log.error("Kafka 전송 실패 - topic={}, payload={}", topic, payload, ex);
                        return null;
                    });

        } catch (Exception e) {
            log.error("Kafka 직렬화 or 전송 중 예외 발생 - event={}", event, e);
        }
    }

}