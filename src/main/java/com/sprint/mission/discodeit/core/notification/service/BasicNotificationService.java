package com.sprint.mission.discodeit.core.notification.service;

import com.sprint.mission.discodeit.core.notification.NotificationDto;
import com.sprint.mission.discodeit.core.notification.dto.NotificationEvent;
import com.sprint.mission.discodeit.core.notification.entity.Notification;
import com.sprint.mission.discodeit.core.notification.repository.JpaNotificationRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final JpaUserRepository userRepository;
  private final JpaNotificationRepository notificationRepository;

  @Override
  @CacheEvict(cacheNames = "notifications", allEntries = true)
  public void create(NotificationEvent event) {
    User receiver = userRepository.findByUserId(event.receiverId());
    Notification notification = Notification.create(receiver, event.title(), event.content(),
            event.type())
        .targetId(event.targetId())
        .build();
    notificationRepository.save(notification);
  }

  @Override
  @Cacheable(cacheNames = "notifications", key = "#userId")
  public List<NotificationDto> findAll(UUID userId) {
    List<Notification> notificationList = notificationRepository.findAllByUserId(userId);
    return notificationList.stream().map(NotificationDto::from).toList();
  }

  @Override
  public void deleteById(UUID notificationId) {
    Notification notification = notificationRepository.findByNotificationId(notificationId);
    notificationRepository.delete(notification);
  }
}
