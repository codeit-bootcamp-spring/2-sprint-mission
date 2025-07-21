package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.NotificationMapper; // Mapper는 직접 만들어야 함
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationMapper notificationMapper; // MapStruct Mapper

  @Async("notificationTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleNewMessage(NewMessageNotificationEvent event) {
    Message message = event.message();
    List<ReadStatus> subscribedUsers = readStatusRepository.findAllByChannelIdWithUser(message.getChannel().getId());

    for (ReadStatus status : subscribedUsers) {
      if (status.isNotificationEnabled() && !status.getUser().getId().equals(message.getAuthor().getId())) {
        String title = String.format("새 메시지: %s", message.getChannel().getName());
        String content = String.format("%s: %s", message.getAuthor().getUsername(), message.getContent());
        Notification notification = new Notification(status.getUser(), title, content, NotificationType.NEW_MESSAGE, message.getChannel().getId());
        notificationRepository.save(notification);
        log.info("{}님에게 새 메시지 알림 생성", status.getUser().getUsername());
      }
    }
  }

  @Override
  @Cacheable(value = "userNotifications", key = "#receiverId")
  public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
    return notificationRepository.findAllByReceiverId(receiverId).stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  @Override
  @CacheEvict(value = "userNotifications", key = "#userId")
  public void delete(UUID notificationId, UUID userId) {
    notificationRepository.deleteById(notificationId);
  }

  @Override
  public boolean isOwner(UUID notificationId, UUID userId) {
    return notificationRepository.findById(notificationId)
        .map(n -> n.getReceiver().getId().equals(userId))
        .orElse(false);
  }
}
