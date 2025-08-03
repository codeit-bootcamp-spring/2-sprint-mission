package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
  SseEmitter subscribe(UUID userId, String lastEventId);

  void sendNotification(NotificationDto notificationDto);

  void sendBinaryContentStatus(UUID uploaderId, BinaryContentDto binaryContentDto);

  void broadcast(String eventName, Object data);

}
