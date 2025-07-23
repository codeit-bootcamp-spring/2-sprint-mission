package com.sprint.mission.discodeit.domain.notification.service;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationResult;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

  List<NotificationResult> getAllByUserId(UUID userId);

  void delete(UUID notificationId);

}
