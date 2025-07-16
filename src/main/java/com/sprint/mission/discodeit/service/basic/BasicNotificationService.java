package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> getNotifications(UUID userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteNotification(UUID userId, UUID notificationId) {
        notificationRepository.deleteByIdAndReceiverId(notificationId, userId);
    }
}
