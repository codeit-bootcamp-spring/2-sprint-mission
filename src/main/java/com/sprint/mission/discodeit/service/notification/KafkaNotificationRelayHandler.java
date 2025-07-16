package com.sprint.mission.discodeit.service.notification;

import com.sprint.mission.discodeit.dto.data.NotificationMessage;
import com.sprint.mission.discodeit.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationRelayHandler {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    private final static String TOPIC = "notification-topic";

    @EventListener
    public void handle(Notification event) {
        NotificationMessage message = NotificationMessage.from(event);
        kafkaTemplate.send(TOPIC, message.getReceiverId(), message);
    }
}
