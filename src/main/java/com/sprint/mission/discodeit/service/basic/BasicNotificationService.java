package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    @Cacheable(value = "notifications", key = "'Notifications_' + #p0")
    public List<NotificationDto> getNotifications(UUID userId) {
        log.info("Getting notifications for {}", userId);
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "notifications", key = "'Notifications_' + #p1")
    @Transactional
    public void deleteNotification(UUID userId, UUID notificationId) {
        log.info("deleteNotification notifications for {}", userId);
        notificationRepository.deleteByIdAndReceiverId(notificationId, userId);
    }
}
