package com.sprint.mission.discodeit.domain.notification.service.impl;

import static com.sprint.mission.discodeit.common.config.CacheConfig.NOTIFICATION_CACHE_NAME;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationResult;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Cacheable(value = NOTIFICATION_CACHE_NAME, key = "#userId", condition = "#userId != null")
  @Transactional(readOnly = true)
  @Override
  public List<NotificationResult> getAllByUserId(UUID userId) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ex) {

    }

    List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
    return NotificationResult.from(notifications);
  }

  @CacheEvict(value = NOTIFICATION_CACHE_NAME, allEntries = true)
  @Override
  public void delete(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }

}
