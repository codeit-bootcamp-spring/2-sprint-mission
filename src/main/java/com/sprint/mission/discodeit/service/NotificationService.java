package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> findByRecevierId(UUID receiverId);

  void delete(UUID notificationId, UUID receiverId) throws AccessDeniedException;

  void save(Notification notification);
}
