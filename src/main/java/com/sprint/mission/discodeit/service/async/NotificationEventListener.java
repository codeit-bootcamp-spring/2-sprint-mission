package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ReadStatusRepository readStatusRepository;

    private final NotificationRepository notificationRepository;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final BinaryContentRepository binaryContentRepository;

    @EventListener
    @Async("combinedExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void onMessageCreated(MessageCreatedEvent event) {
        readStatusRepository
                .findByChannelIdAndNotificationEnabledTrue(event.getChannelId())
                .forEach(rs -> {
                    Notification notification = new Notification(
                            rs.getUser().getId(),
                            "새 메시지가 도착했어요",
                            event.getMessage(),
                            NotificationType.NEW_MESSAGE,
                            event.getChannelId()
                    );

                    notificationRepository.save(notification);
                });
    }


    @Async("combinedExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void onUserCreated(RoleChangedEvent event) {

        Notification notification = new Notification(
                event.getUserId(),
                "새 메시지가 도착했어요",
                String.format("%s -> %s", event.getOldRole(), event.getNewRole()),
                NotificationType.ROLE_CHANGED,
                event.getUserId()
        );

        notificationRepository.save(notification);
    }

}
