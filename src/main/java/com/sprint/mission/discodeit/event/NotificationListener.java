package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;
  private final CacheManager cacheManager;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      retryFor = { DataAccessException.class, TransientDataAccessException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  public void handleMessageNotificationEvent(MessageNotificationEvent event) {

    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelIdWithUser(event.targetId());
    Set<User> userSet = readStatusList.stream()
        .map(ReadStatus::getUser)
            .collect(Collectors.toSet());
    String title = event.title();
    String content = event.content();
    NotificationType type = event.type();
    UUID targetId = event.targetId();

    List<Notification> notificationList = userSet.stream()
        .map(user ->
          new Notification(user, title, content, type, targetId)
        ).toList();

    notificationRepository.saveAll(notificationList);

    Cache cache = Objects.requireNonNull(cacheManager.getCache("notifications"), "Cache 'notifications' not found");
    userSet.forEach(user -> cache.evictIfPresent(user.getId() + "_notifications"));
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      retryFor = { DataAccessException.class, TransientDataAccessException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  public void handleRoleChangeNotificationEvent(RoleChangeNotificationEvent event) {
    User user = userRepository.findById(event.targetId())
        .orElseThrow(() -> {
          log.warn("User with id {} not found", event.targetId());
          return UserNotFoundException.withId(event.targetId());
        }
    );
    String title = event.title();
    String content = event.content();
    NotificationType type = event.type();
    UUID targetId = event.targetId();

    Notification newNotification = new Notification(user, title, content, type, targetId);
    notificationRepository.save(newNotification);

    Cache cache = Objects.requireNonNull(cacheManager.getCache("notifications"), "Cache 'notifications' not found");
    cache.evictIfPresent(user.getId() + "_notifications");
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  @Retryable(
      retryFor = { DataAccessException.class, TransientDataAccessException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  public void handleAsyncFailureNotificationEvent(AsyncFailureNotificationEvent event) {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserDto userDto = userDetails.getUserDto();
    UUID userId = userDto.id();
    User receiver = userRepository.findById(userId).orElseThrow( () -> {
          log.warn("User with id {} not found", userId);
          return UserNotFoundException.withId(userId);
        }
    );

    String title = event.title();
    String content = event.content();
    NotificationType type = event.type();
    UUID targetId = null;

    Notification newNotification = new Notification(receiver, title, content, type, targetId);
    notificationRepository.save(newNotification);

    Cache cache = Objects.requireNonNull(cacheManager.getCache("notifications"), "Cache 'notifications' not found");
    cache.evictIfPresent(receiver.getId() + "_notifications");
  }

  @Recover
  public void recover(DataAccessException ex, MessageNotificationEvent event) {
    log.error("알림 저장 비동기 처리 실패: 채널 ID = {}, 원인: {}", event.targetId(), ex.getMessage(), ex);
  }

}
