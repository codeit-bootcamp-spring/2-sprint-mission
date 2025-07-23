package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Cacheable(value = "userNotifications", key = "#currentUserId")
    @Override
    public List<NotificationDto> getMyNotifications(UUID currentUserId) {
        return notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(currentUserId)
            .stream()
            .map(notificationMapper::toDto)
            .toList();
    }

    @CacheEvict(value = "userNotifications", key = "#currentUserId")
    @Override
    public void deleteMyNotification(UUID notificationId, UUID currentUserId) {
        notificationRepository.deleteByIdAndReceiverId(currentUserId, notificationId);
    }
}
