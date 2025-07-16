package com.sprint.mission.discodeit.core.notification.repository;

import com.sprint.mission.discodeit.core.notification.entity.Notification;
import java.util.List;
import java.util.UUID;

public interface CustomNotificationRepository {

  Notification findByNotificationId(UUID notificationId);

  List<Notification> findAllByUserId(UUID userId);

}
