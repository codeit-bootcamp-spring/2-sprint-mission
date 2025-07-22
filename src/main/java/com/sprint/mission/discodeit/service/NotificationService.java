package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationService {

  List<NotificationDto> findAllByReceiverId(UUID receiverId);

  void delete(UUID notificationId, UUID receiverId);

  void createNewMessageNotification(UUID receiverId, UUID channelId,
      UUID messageId, String authorName);

  void createRoleChangedNotification(UUID receiverId, String oldRole, String newRole);

  void createAsyncFailedNotification(UUID receiverId, String taskName, String failureReason);
}
