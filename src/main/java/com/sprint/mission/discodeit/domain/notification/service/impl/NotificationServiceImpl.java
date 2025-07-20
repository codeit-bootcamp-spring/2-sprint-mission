package com.sprint.mission.discodeit.domain.notification.service.impl;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationResult;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Override
  public List<NotificationResult> getAllByUserId(UUID userId) {
    List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
    return NotificationResult.from(notifications);
  }

  @Override
  public void delete(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }

}
