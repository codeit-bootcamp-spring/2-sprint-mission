package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.NotificationEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CacheEvict(value = "notifications", key = "#event.receiverId")
    public void handleNotificationEvent(NotificationEvent event) {
        User receiver = userRepository.findById(event.receiverId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = Notification.builder()
                .receiver(receiver)
                .type(event.type())
                .targetId(event.targetId())
                .content(event.content())
                .build();

        notificationRepository.save(notification);
    }
} 