package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.controller.notification.NotificationDto;
import com.sprint.mission.discodeit.dto.service.notification.CreateNotificationParam;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.auth.AccessDeniedException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicNotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationMapper notificationMapper;

  @Transactional
  public NotificationDto create(CreateNotificationParam param) {
    Notification notification = Notification.builder()
        .title(param.title())
        .content(param.content())
        .receiver(userRepository.findById(param.receiverId())
            .orElseThrow(() -> {
              log.warn("User not found - id:{}, details:{}", param.receiverId(),
                  "notification create");
              return new UserNotFoundException(Map.of("id", param.receiverId()));
            })
        )
        .targetId(param.targetId())
        .type(param.type())
        .build();

    return notificationMapper.toNotificationDto(notificationRepository.save(notification));
  }

  @Transactional
  public List<NotificationDto> findAll() {
    // 현재 인증 정보 꺼내서
    UUID userId = getCurrentUserId();

    // 본인의 알림만 조회
    return notificationRepository.findAllByReceiver_Id(userId).stream()
        .map(notificationMapper::toNotificationDto)
        .toList();
  }

  @Transactional
  public void delete(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> {
          log.warn("Notification not found - id:{}, details:{}", notificationId,
              "notification delete");
          return new NotificationNotFoundException(Map.of("id", notificationId));
        });

    UUID userId = getCurrentUserId();

    // 자신의 것이 아닌 알림을 지우려고 하면 예외 발생
    if (!notification.getReceiver().getId().equals(userId)) {
      throw new AccessDeniedException(Map.of("id", userId, "details", "notification delete"));
    }
    notificationRepository.delete(notification);
  }

  private UUID getCurrentUserId() {
    return ((CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal()).getUserDto().id();
  }
}
