package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.event.NotificationEvent;

public interface NotificationCommandService {
  void create(NotificationEvent event);
}
