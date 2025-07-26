package com.sprint.mission.discodeit.common.event.handler;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
//@Component
@RequiredArgsConstructor
public class KafkaProducer {

  public static final String KAFKA_TOPIC_NAME = "discodeit.notification";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

//  @Async("kafkaExecutor")
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void relayNotificationEvent(NotificationEvent event) {
//    try {
//
//      String jsonMessage = objectMapper.writeValueAsString(event);
//
//      kafkaTemplate.send(KAFKA_TOPIC_NAME, jsonMessage);
//
//      log.info("NotificationEvent를 Kafka로 중계 전송 완료 - Topic: {}, ReceiverId: {}",
//          KAFKA_TOPIC_NAME, event.getReceiverId());
//
//    } catch (Exception ex) {
//      log.error("NotificationEvent Kafka 중계 전송 실패 - ReceiverId: {}", event.getReceiverId(), ex);
//    }
//  }
//
//  @Recover
//  public void recover(Exception ex, NotificationEvent event) {
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

}
