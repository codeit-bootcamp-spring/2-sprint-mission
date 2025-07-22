package com.sprint.mission.discodeit.core.notification.service;

import com.sprint.mission.discodeit.core.notification.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

  private final NotificationService notificationService;


  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("notificationExecutor") // 비동기 실행
  @Retryable(
      value = {RuntimeException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000) // 1초 간격으로 재시도
  )
  public void eventPublish(NotificationEvent event) {
    log.info("알림 생성 시도: title={}, content={}", event.title(), event.content());
    notificationService.create(event);
    log.info("알림 생성 성공");
  }

  @Recover
  public void recover(RuntimeException e, NotificationEvent event) {
    // 에러 로그를 기록하여 나중에 수동으로 처리할 수 있도록 합니다.
    log.info("알림 생성 실패: title={}", event.title(), e);
  }
}
