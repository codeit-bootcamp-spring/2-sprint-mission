package com.sprint.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEventPayload;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SseService sseService;
    private final NotificationMapper notificationMapper;

    @KafkaListener(
            topics = "notification-topic",
            groupId = "notification-consumer-group"
    )
    public void consume(String message) {
        try {
            NotificationEventPayload event = objectMapper.readValue(message, NotificationEventPayload.class);

            User receiver = userRepository.findById(event.receiverId())
                    .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

            // DB 저장
            Notification notification = notificationService.sendNotification(
                    receiver,
                    event.title(),
                    event.content(),
                    event.type(),
                    event.targetId()
            );

            NotificationDto dto = notificationMapper.toDto(notification);
            // SSE 실시간 push
            sseService.sendEvent(receiver.getId().toString(), "notifications", dto);

            log.info("알림 처리 완료 - user={}, title={}", receiver.getUsername(), event.title());

        } catch (Exception e) {
            log.error("Kafka 알림 메시지 처리 실패 - payload={}", message, e);
            // 필요 시 실패 큐 전송 or 저장 가능
        }
    }
}