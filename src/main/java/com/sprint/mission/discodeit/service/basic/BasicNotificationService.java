package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "notificationList", key = "#userId")
    public List<NotificationDto> getNotifications(UUID userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId).stream()
            .map(notificationMapper::toDto)
            .toList();
    }
    @Override
    @Transactional
    public void deleteNotification(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if(!notification.getReceiver().getId().equals(userId)){
            throw new DiscodeitException(ErrorCode.UNAUTHORIZED_USER);
        }
        notificationRepository.delete(notification);
    }
}
