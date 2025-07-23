package com.sprint.mission.discodeit.domain.notification.service;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationDto;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> findAllByReceiverId(UUID receiverId);

  void delete(UUID notificationId, UUID receiverId);

  void create(UUID receiverId, String title, String content, NotificationType notificationType,
      UUID targetId);

  void createAll(Set<UUID> receiverIds, String title, String content,
      NotificationType notificationType, UUID targetId);
} 