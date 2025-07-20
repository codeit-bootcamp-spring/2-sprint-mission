package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncTaskFailureEventListener {

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Async("asyncTaskFailureExecutor")
  public void handleAsyncFailure(AsyncTaskFailureEvent event) {
    log.info("비동기 작업 실패 로그 기록 시작: requestId={}", event.requestId());
    try {
      AsyncTaskFailure failure = new AsyncTaskFailure(
          event.taskName(),
          event.requestId(),
          event.failureReason()
      );
      asyncTaskFailureRepository.save(failure);
    } catch (Exception e) {
      log.error("비동기 작업 실패 로그 기록 중 오류 발생: requestId={}", event.requestId(), e);
    }
  }
}