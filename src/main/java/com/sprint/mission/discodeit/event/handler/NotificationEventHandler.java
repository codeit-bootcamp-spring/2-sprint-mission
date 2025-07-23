//package com.sprint.mission.discodeit.event.handler;
//
//import com.sprint.mission.discodeit.event.NotificationEvent;
//import com.sprint.mission.discodeit.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.retry.annotation.Recover;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class NotificationEventHandler {
//
//  private final NotificationService notificationService;
//
//  @Async
//  @Retryable(
//      value = Exception.class,
//      maxAttempts = 3,
//      backoff = @Backoff(delay = 2000)
//  )
//  @TransactionalEventListener
//  public void handle(NotificationEvent event) {
//    log.info("알림 이벤트 수신: receiverId={}, type={}", event.receiverId(), event.notificationType());
//    try {
//      notificationService.createNotification(
//          event.receiverId(),
//          event.notificationType(),
//          event.targetId(),
//          event.notificationInfo()
//      );
//    } catch (Exception e) {
//      log.error("알림 이벤트 처리 실패. 재시도합니다. receiverId: {}, error: {}", event.receiverId(),
//          e.getMessage());
//      throw e; // Retryable이 동작하려면 예외가 다시 던져져야 함
//    }
//  }
//
//  @Recover
//  public void recover(Exception e, NotificationEvent event) {
//    log.error("알림 생성 최종 실패. event: {}, error: {}", event, e.getMessage());
//  }
//
//}
