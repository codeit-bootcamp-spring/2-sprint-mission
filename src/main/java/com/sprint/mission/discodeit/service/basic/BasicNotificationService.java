package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Notification;
import com.sprint.mission.discodeit.domain.NotificationType;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.sse.SseEvent;
import com.sprint.mission.discodeit.sse.service.SseLocalService;
import com.sprint.mission.discodeit.util.SecurityUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@CacheConfig(cacheNames = "notificationsByUser")
@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final UserRepository userRepository;
  private final SseLocalService sseLocalService;

  @CacheEvict(allEntries = true)
  @Transactional
  @Override
  public NotificationDto createNotification(UUID receiverId, NotificationType type, UUID targetId,
      Map<String, Object> notificationInfo) {
    User user = userRepository.findById(receiverId)
        .orElseThrow(() -> UserNotFoundException.byId(receiverId));

    String title = generateTitle(type, notificationInfo);
    String content = generateContent(type, notificationInfo);

    Notification notification = new Notification(
        user,
        title,
        content,
        type,
        targetId
    );

    notificationRepository.save(notification);

    NotificationDto notificationDto = notificationMapper.toDto(notification);

    UUID eventId = UUID.randomUUID();
    Map<String, Object> eventData = Map.of(
        "NotificationDto", notificationDto
    );
    SseEvent sseEvent = new SseEvent(eventId, "notifications", eventData);
    sseLocalService.sendEventToUser(receiverId, sseEvent);

    return notificationDto;
  }

  @Cacheable(key = "T(com.sprint.mission.discodeit.util.SecurityUtils).getCurrentUserId()")
  @Override
  @Transactional(readOnly = true)
  public List<NotificationDto> findAll() {
    UUID userId = SecurityUtils.getCurrentUserId();
    return notificationRepository.findByUserId(userId)
        .stream()
        .map(notificationMapper::toDto)
        .collect(Collectors.toList());
  }

  @CacheEvict(key = "#notificationId")
  @Transactional
  @Override
  public void deleteNotification(UUID notificationId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    if (!notificationRepository.existsByIdAndUserId(notificationId, userId)) {
      throw new AccessDeniedException("이 알림을 삭제할 권한이 없습니다.");
    }
    notificationRepository.deleteById(notificationId);
  }

  private String generateTitle(NotificationType type, Map<String, Object> notificationInfo) {
    return switch (type) {
      case NEW_MESSAGE -> (String) notificationInfo.get("authorName");
      case ROLE_CHANGED, ASYNC_FAILED -> type.getTitle();
    };
  }

  private String generateContent(NotificationType type, Map<String, Object> notificationInfo) {
    return switch (type) {
      case NEW_MESSAGE -> (String) notificationInfo.get("message");
      case ROLE_CHANGED -> {
        String previousRole = (String) notificationInfo.get("previousRole");
        String newRole = (String) notificationInfo.get("newRole");
        yield String.format("[%s] → [%s]", previousRole, newRole);
      }
      case ASYNC_FAILED -> {
        String method = (String) notificationInfo.get("method");
        String error = (String) notificationInfo.get("error");
        yield String.format("비동기 작업 실패: [%s]에서 '%s'", method, error);
      }
    };
  }
}
