package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Override
  @Cacheable(value = "notifications", key = "#userId")
  @Transactional(readOnly = true)
  public List<NotificationDto> findByReceiverId(UUID userId) {
    return notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(userId).stream()
        .map(n -> new NotificationDto(n.getId(), n.getType(), n.getTargetId(), n.getCreatedAt()))
        .toList();
  }

  @Override
  @CacheEvict(value = "notifications", key = "#userId")
  @Transactional
  public void delete(UUID notificationId, UUID userId) {
    try {
      notificationRepository.deleteByIdAndReceiverId(notificationId, userId);
    } catch (Exception e) {
      throw new DiscodeitException(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
  }
}