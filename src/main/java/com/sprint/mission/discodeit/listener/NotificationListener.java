package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Async("eventTaskExecutor")
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleNotification(NotificationEvent event) {
        try {
            User receiver = userRepository.findById(event.getReceiverId())
                    .orElseThrow(() -> UserNotFoundException.withId(event.getReceiverId()));

            Notification notification = new Notification(
                    receiver,
                    event.getTitle(),
                    event.getContent(),
                    event.getType(),
                    event.getTargetId()
            );
            notificationRepository.save(notification);
            log.info("Notification saved successfully for receiver: {}", event.getReceiverId());
        } catch (Exception e) {
            log.error("Failed to process notification event for receiver {}. Attempting retry...", event.getReceiverId(), e);
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, NotificationEvent event) {
        log.error("Notification recovered successfully for receiver {}", event.getReceiverId(), e);
        notificationService.createNotificationAsyncFailed(event.getReceiverId());
    }



}
