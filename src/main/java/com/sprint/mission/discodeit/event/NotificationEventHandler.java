package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.service.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

  private final NotificationCommandService notificationCommandService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NotificationEvent event) {
    log.info("🔔 알림 이벤트 수신: {}", event);
    notificationCommandService.create(event);
  }
}
