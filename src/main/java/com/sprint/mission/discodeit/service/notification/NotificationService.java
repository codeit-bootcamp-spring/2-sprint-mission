package com.sprint.mission.discodeit.service.notification;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.AccessDeniedException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;

import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationEventPublisher notificationEventPublisher;

    @Cacheable(cacheNames = "notifications", key = "'notification_'+#userId")
    public List<NotificationDto> getNotificationsForUser(UUID userId) {
        return notificationRepository.findByReceiverId(userId).stream()
            .map(NotificationDto::from)
            .toList();
    }

    @Transactional
    @CacheEvict(cacheNames = "notifications", key = "#event.receiver.id")
    public void deleteNotification(UUID userId, UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> NotificationNotFoundException.withUserIdAndNotificationId
                (userId, notificationId));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw AccessDeniedException.withUserIdAndId(userId, notification.getReceiver().getId());
        }

        notificationRepository.delete(notification);
    }

    public void sendNotification(Notification event) {
        notificationRepository.save(event);
    }
}
