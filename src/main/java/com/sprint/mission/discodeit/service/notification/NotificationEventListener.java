package com.sprint.mission.discodeit.service.notification;

import com.sprint.mission.discodeit.dto.data.NotificationMessage;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final UserRepository userRepository;

    @KafkaListener(
        topics = "discodeit.notification",
        groupId = "notification-consumer-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Retryable(
        value = Exception.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public void consume(NotificationMessage message) throws NotFoundException {
        try {
            log.info("Kafka 메시지 수신 - 사용자 ID: {}", message.getReceiverId());
            User receiver = userRepository.findById(message.getReceiverId())
                .orElseThrow(() -> UserNotFoundException.withId(message.getReceiverId()));

            Notification notification = message.toEntity(receiver);
            notificationService.sendNotification(notification);

        } catch (Exception e) {
            throw e; // Retryable이 처리
        }
    }

    @Recover
    public void recover(Exception e, NotificationMessage message) {
        log.error("알림 Kafka 메시지 처리 실패. 실패 기록 저장: {}", e.getMessage());
        AsyncTaskFailure failure = new AsyncTaskFailure(
            "KafkaNotificationConsumer",
            MDC.get("requestId"),
            e.getMessage()
        );
        asyncTaskFailureRepository.save(failure);
    }
}
