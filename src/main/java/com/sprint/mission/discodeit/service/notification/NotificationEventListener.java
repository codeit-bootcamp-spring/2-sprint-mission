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
import org.springframework.context.event.EventListener;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;

    @Async
    @EventListener
    @Retryable(
        value = Exception.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public void handle(Notification event) {
        log.info("Processing NotificationEvent for {}", event.getReceiver().getId());
        notificationService.sendNotification(event);
    }

    @Recover
    public void recover(Exception e, Notification event) {
        log.error("알림 처리 실패, 실패 알림 기록: {}", e.getMessage());

        // 실패 기록 저장
        AsyncTaskFailure failure = new AsyncTaskFailure(
            "NotificationEvent",
            MDC.get("requestId"),
            e.getMessage()
        );
        // 저장소 주입 필요
        asyncTaskFailureRepository.save(failure);
    }
}
