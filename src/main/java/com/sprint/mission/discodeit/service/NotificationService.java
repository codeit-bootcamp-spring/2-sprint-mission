package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationService {

  NotificationDto create(UUID receiverId, String title, String content, NotificationType type,
      UUID targetId);

  List<Notification> createAll(
      Set<UUID> receiverIds, String title, String content, NotificationType type, UUID targetId);

  List<NotificationDto> findAll(UUID userId);

  void delete(UUID receiverId, UUID notificationId);
}
