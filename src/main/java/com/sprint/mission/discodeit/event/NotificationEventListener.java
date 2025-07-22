package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.service.NotificationService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private NotificationService notificationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNotification(NotificationEvent event) {
    Notification notification = Notification.builder()
        .receiverId(event.getReceiverId())
        .title(event.getTitle())
        .content(event.getContent())
        .type(event.getType())
        .targetId(event.getTargetId())
        .build();

    notificationService.save(notification);
  }
}
