package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.NotificationType;
import com.sprint.mission.discodeit.dto.NotificationDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NotificationService {

  NotificationDto createNotification(UUID receiverId, NotificationType type, UUID targetId,
      Map<String, Object> notificationInfo);

  List<NotificationDto> findAll();

  void deleteNotification(UUID notificationId);

}
