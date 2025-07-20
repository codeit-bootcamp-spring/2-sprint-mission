// 1. BasicNotificationService.java (최종 버전)
package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.notification.NotificationOwnershipException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Transactional(readOnly = true)
  @Override
  public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
    log.debug("알림 목록 조회 시작: receiverId={}", receiverId);

    List<Notification> notifications = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(
        receiverId);

    List<NotificationDto> result = notifications.stream()
        .map(notificationMapper::toDto)
        .toList();

    log.info("알림 목록 조회 완료: receiverId={}, count={}", receiverId, result.size());
    return result;
  }

  @Transactional
  @Override
  public void delete(UUID notificationId, UUID receiverId) {
    log.debug("알림 삭제 시작: notificationId={}, receiverId={}", notificationId, receiverId);

    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> NotificationNotFoundException.withId(notificationId));

    if (!notification.getReceiverId().equals(receiverId)) {
      throw NotificationOwnershipException.withIds(notificationId, receiverId);
    }

    notificationRepository.delete(notification);
    log.info("알림 삭제 완료: notificationId={}, receiverId={}", notificationId, receiverId);
  }

}