package com.sprint.mission.discodeit.common.event.handler;

import static com.sprint.mission.discodeit.common.event.handler.KafkaProducer.KAFKA_TOPIC_NAME;
import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

@Slf4j
//@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  private static final String CONSUMER_GROUP_ID = "notification-service";

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final NotificationRepository notificationRepository;
  private final ObjectMapper objectMapper;

//  @Async("notificationExecutor")
//  @KafkaListener(topics = KAFKA_TOPIC_NAME, groupId = CONSUMER_GROUP_ID)
//  @Retryable(
//      retryFor = Exception.class,
//      maxAttempts = 3,
//      backoff = @Backoff(delay = 1000, multiplier = 2)
//  )
//  public void handleNotificationEvent(String message) {
//    try {
//      NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);
//
//      createNotification(event);
//
//    } catch (Exception ex) {
//      log.error("Kafka 알림 처리 실패", ex);
//    }
//  }
//
//  @Recover
//  public void recover(Exception ex, String message) {
//    String requestId = MDC.get(REQUEST_ID);
//    String errorType = ex.getClass().getSimpleName();
//
//    CompletableFuture.runAsync(() -> {
//      AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure(
//          "CREATE_NOTIFICATION_FROM_EVENT", requestId, errorType
//      );
//      asyncTaskFailureRepository.save(asyncTaskFailure);
//    });
//  }
//
//  private Notification createNotification(NotificationEvent event) {
//    Notification notification = new Notification(
//        event.getReceiverId(),
//        event.getTitle(),
//        event.getContent(),
//        event.getType(),
//        event.getTargetId()
//    );
//    return notificationRepository.save(notification);
//  }

}
