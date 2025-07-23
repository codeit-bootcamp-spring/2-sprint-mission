package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
  List<NotificationDto> getNotifications(UserDto userDto);

  void deleteNotification(UUID notificationId);
}
