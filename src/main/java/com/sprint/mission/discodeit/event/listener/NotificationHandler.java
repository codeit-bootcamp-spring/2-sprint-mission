package com.sprint.mission.discodeit.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.service.notification.CreateNotificationParam;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.CreateNotificationEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
// Kafka에 저장된 메시지를 받아서 처리 (Consumer)
public class NotificationHandler {

  private final BasicNotificationService notificationService;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ObjectMapper objectMapper;

  // 컨슈머 메서드 - 해당 토픽에 쌓이는 메시지를 수신해서 처리
  // 여러 인스턴스가 같은 topic, 같은 groupId를 사용하면 "컨슈머 그룹"이 형성됨
  // 컨슈머 그룹 내부에서는 메시지가 분배되어 처리되므로, 같은 메시지는 오직 한 인스턴스에서만 읽어서 소비
  // groupId가 없거나 매번 다르게 설정되면, 같은 메시지가 인스턴스마다 각각 중복 처리됨
  // 즉, 여러 서버(인스턴스)에서 Kafka 컨슈머를 띄우고, 같은 Kafka topic을 바라볼 때, groupId가 같으면,
  //→ Kafka가 컨슈머들을 "한 그룹"으로 묶어서
  //→ 그 그룹 안의 컨슈머(서버)들에게 메시지를 분배
  //→ 같은 메시지는 그룹 내에서 단 한 번만, 한 인스턴스에서만 처리됨.
  @KafkaListener(topics = "discodeit.notification-create", groupId = "notification-service-group")
  @Async("asyncExecutor")
  @Retryable(
      maxAttempts = 3,
      recover = "createAsyncTaskFailure",
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleCreateNotificationEvent(String message) {
    log.info("Kafka message received: {}", message);
    try {
      // JSON 문자열 메시지를 객체로 역직렬화
      CreateNotificationEvent notificationEvent = objectMapper.readValue(
          message,
          CreateNotificationEvent.class
      );
      notificationService.create(CreateNotificationParam.builder()
          .type(notificationEvent.type())
          .content(notificationEvent.content())
          .title(notificationEvent.title())
          .receiverId(notificationEvent.receiverId())
          .targetId(notificationEvent.targetId())
          .build());
    } catch (Exception e) {
      log.error("Failed to process notification message: {}", e.getMessage(), e);
      throw new RuntimeException(e); // @Retryable을 위해 예외 던짐
    }
  }

  @Recover
  @Transactional
  public void createAsyncTaskFailure(Exception e, String message) {
    AsyncTaskFailure asyncTaskFailure = AsyncTaskFailure.builder()
        .failureReason(e.getMessage())
        .requestId(MDC.get("requestId") != null ? MDC.get("requestId").toString() : "UNKNOWN")
        .taskName("NotificationCreate")
        .build();

    asyncTaskFailureRepository.save(asyncTaskFailure);

    log.error("Notification create retry finally failed - requestId: {}", MDC.get("requestId"));

    UUID receiverId = null;
    try {
      CreateNotificationEvent notificationEvent =
          objectMapper.readValue(message, CreateNotificationEvent.class);
      receiverId = notificationEvent.receiverId();
    } catch (JsonProcessingException ex) {
      log.warn("Failed to deserialize JSON: {}", ex.getMessage(), ex);
    }

    notificationService.create(CreateNotificationParam.builder()
        .type(NotificationType.ASYNC_FAILED)
        .receiverId(receiverId)
        .title("백그라운드 작업이 실패했습니다.")
        .content(e.getMessage())
        .targetId(null)
        .build());
  }
}
