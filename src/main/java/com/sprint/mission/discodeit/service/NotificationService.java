package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationCreateAllDto;
import com.sprint.mission.discodeit.dto.notification.NotificationCreateDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public interface NotificationService {

    void createNotification(NotificationCreateDto notificationCreateDto);

    void createAllNotifications(NotificationCreateAllDto notificationCreateAllDto);

    NotificationDto readNotification(UUID notificationId);

    List<NotificationDto> readNotifications(Authentication auth);

    void deleteNotification(UUID notificationId);

}
