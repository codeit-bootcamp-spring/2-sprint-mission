package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationDto> getNotificationsForUser(UUID userId);

    void deleteNotification(UUID notificationId, UUID userId);

    void sendNotification(User receiver, String title, String content, NotificationType type, UUID targetId);
}
