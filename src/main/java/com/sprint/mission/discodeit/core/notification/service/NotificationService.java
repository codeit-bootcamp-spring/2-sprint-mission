package com.sprint.mission.discodeit.core.notification.service;

import com.sprint.mission.discodeit.core.notification.NotificationDto;
import com.sprint.mission.discodeit.core.notification.dto.NotificationEvent;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  void create(NotificationEvent event);

  List<NotificationDto> findAll(UUID userId);

  void deleteById(UUID notificationId);
}
