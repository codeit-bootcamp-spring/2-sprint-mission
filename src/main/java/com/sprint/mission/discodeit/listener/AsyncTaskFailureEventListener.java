package com.sprint.mission.discodeit.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncTaskFailureEventListener {

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "async-task.failure", groupId = "async-task-failure-group")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleAsyncFailure(String kafkaEvent) throws JsonProcessingException {
    AsyncTaskFailureEvent event = objectMapper.readValue(kafkaEvent, AsyncTaskFailureEvent.class);
    log.info("비동기 작업 실패 로그 기록 시작: requestId={}", event.requestId());
    try {
      String failureReason = Objects.toString(event.failureReason(), "");

      AsyncTaskFailure failure = new AsyncTaskFailure(
          event.taskName(),
          event.requestId(),
          failureReason
      );
      asyncTaskFailureRepository.save(failure);
    } catch (Exception e) {
      log.error("비동기 작업 실패 로그 기록 중 오류 발생: requestId={}", event.requestId(), e);
    }
  }
}