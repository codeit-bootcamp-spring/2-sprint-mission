package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> findNotificationsByUser(User user);
    void deleteNotification(UUID notificationId, User user);
} 