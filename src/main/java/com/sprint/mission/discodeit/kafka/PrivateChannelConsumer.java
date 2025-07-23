package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.PrivateChannelCreatedEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrivateChannelConsumer {

    @KafkaListener(topics = "private-channel-topic", groupId = "channel-group")
    public void consume(String message) {
        try {
            PrivateChannelCreatedEventPayload payload = new ObjectMapper().readValue(message, PrivateChannelCreatedEventPayload.class);
            log.info("채널 생성 이벤트 수신: {}", payload);
        } catch (Exception e) {
            log.error("채널 Kafka 메시지 처리 실패", e);
        }
    }
}