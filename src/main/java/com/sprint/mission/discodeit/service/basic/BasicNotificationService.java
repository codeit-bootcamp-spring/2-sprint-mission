package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.sse.SseNotificationEvent;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.notification.NotificationOwnershipException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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
  private final ApplicationEventPublisher eventPublisher;

  @Transactional(readOnly = true)
  @Cacheable(value = "notifications", key = "#receiverId")
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
  @CacheEvict(value = "notifications", key = "#receiverId")
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

  @Transactional
  @CacheEvict(value = "notifications", key = "#receiverId")
  public void createNewMessageNotification(UUID receiverId, UUID channelId, UUID messageId,
      String authorName) {
    log.debug("새 메시지 알림 생성 시작: receiverId={}, channelId={}, messageId={}",
        receiverId, channelId, messageId);

    String title = "새 메시지";
    String content = String.format("%s님이 새 메시지를 보냈습니다.", authorName);

    Notification notification = new Notification(
        receiverId,
        title,
        content,
        NotificationType.NEW_MESSAGE,
        channelId
    );

    notificationRepository.save(notification);
    log.info("새 메시지 알림 생성 완료: receiverId={}, notificationId={} (캐시 무효화)",
        receiverId, notification.getId());

    NotificationDto notificationDto = notificationMapper.toDto(notification);
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
    eventPublisher.publishEvent(new SseNotificationEvent(
        receiverId,
        notificationDto,
        Instant.now(),
        requestId != null ? requestId : UUID.randomUUID().toString()
    ));
  }

  @Transactional
  @CacheEvict(value = "notifications", key = "#receiverId")
  public void createRoleChangedNotification(UUID receiverId, String oldRole, String newRole) {
    log.debug("권한 변경 알림 생성 시작: receiverId={}, oldRole={}, newRole={}",
        receiverId, oldRole, newRole);

    String title = "권한 변경";
    String content = String.format("귀하의 권한이 %s에서 %s로 변경되었습니다.", oldRole, newRole);

    Notification notification = new Notification(
        receiverId,
        title,
        content,
        NotificationType.ROLE_CHANGED,
        receiverId
    );

    notificationRepository.save(notification);
    log.info("권한 변경 알림 생성 완료: receiverId={}, notificationId={} (캐시 무효화)",
        receiverId, notification.getId());

    NotificationDto notificationDto = notificationMapper.toDto(notification);
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
    eventPublisher.publishEvent(new SseNotificationEvent(
        receiverId,
        notificationDto,
        Instant.now(),
        requestId != null ? requestId : UUID.randomUUID().toString()
    ));
  }

  @Transactional
  @CacheEvict(value = "notifications", key = "#receiverId")
  public void createAsyncFailedNotification(UUID receiverId, String taskName,
      String failureReason) {
    log.debug("비동기 작업 실패 알림 생성 시작: receiverId={}, taskName={}", receiverId, taskName);

    String title = "비동기 작업 실패";
    String content = String.format("비동기 작업이 실패했습니다: %s", failureReason);

    Notification notification = new Notification(
        receiverId,
        title,
        content,
        NotificationType.ASYNC_FAILED,
        null
    );

    notificationRepository.save(notification);
    log.info("비동기 작업 실패 알림 생성 완료: receiverId={}, notificationId={} (캐시 무효화)",
        receiverId, notification.getId());

    NotificationDto notificationDto = notificationMapper.toDto(notification);
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
    eventPublisher.publishEvent(new SseNotificationEvent(
        receiverId,
        notificationDto,
        Instant.now(),
        requestId != null ? requestId : UUID.randomUUID().toString()
    ));
  }
}