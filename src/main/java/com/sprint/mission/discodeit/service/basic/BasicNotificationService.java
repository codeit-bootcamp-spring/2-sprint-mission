package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.GroupNotificationEvent;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final ApplicationEventPublisher eventPublisher;

  @CacheEvict(value = "notifications", key = "#receiverId")
  @Override
  @Transactional
  public NotificationDto create(
      UUID receiverId, String title, String content, NotificationType type, UUID targetId) {
    log.debug("알림 생성 시작 - type : {}, targetId : {}", type, targetId);
    Notification notification = new Notification(receiverId, title, content, type, targetId);

    notificationRepository.save(notification);
    log.debug("알림 생성 완료");

    return notificationMapper.toDto(notification);
  }

  @Override
  @Transactional
  public List<Notification> createAll(
      Set<UUID> receiverIds, String title, String content, NotificationType type, UUID targetId) {
    log.debug("단체 알림 생성 시작 - type : {}, targetId : {}", type, targetId);
    List<Notification> notificationList = receiverIds.stream()
        .map(receiverId -> new Notification(receiverId, title, content, type, targetId))
        .toList();

    notificationRepository.saveAll(notificationList);
    eventPublisher.publishEvent(new GroupNotificationEvent(receiverIds));

    log.debug("단체 알림 생성 완료 - {}건", notificationList.size());
    return notificationList;
  }

  @Cacheable(value = "notifications", key = "#receiverId")
  @Override
  @Transactional(readOnly = true)
  public List<NotificationDto> findAll(UUID receiverId) {
    log.debug("사용자의 전체 알림 조회 시작 - userId : {}", receiverId);

    List<Notification> res = notificationRepository.findAllByReceiverId(receiverId);
    log.debug("사용자의 전체 알림 조회 완료");
    return notificationMapper.toDtoList(res);
  }

  @CacheEvict(value = "notifications", key = "#receiverId")
  @Override
  @Transactional
  public void delete(UUID receiverId, UUID notificationId) {
    log.debug("알림 삭제 시작 - receiverId : {}, notificationId : {}", receiverId, notificationId);

    if (receiverId == null || notificationId == null) {
      throw NotificationNotFoundException.withId(notificationId);
    }
    notificationRepository.deleteByIdAndReceiverId(notificationId, receiverId);
  }
}
