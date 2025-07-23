package com.sprint.mission.discodeit.event;

public interface KafkaPublishableEvent {
    String topic(); // Kafka로 보낼 토픽명
}