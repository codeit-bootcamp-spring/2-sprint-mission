package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getNotifications(UUID receiverId);

    void deleteNotification(UUID notificationId, UUID receiverId);

    void createNotificationChannel(UUID receiverId, UUID channelId);

    void createNotificationRoleChanged(UUID receiverId, UUID userId);

    void createNotificationAsyncFailed(UUID receiverId);

}
