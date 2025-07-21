package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationDto> getNotifications(UUID userId);

    void deleteNotification(UUID userId, UUID notificationId);
}
