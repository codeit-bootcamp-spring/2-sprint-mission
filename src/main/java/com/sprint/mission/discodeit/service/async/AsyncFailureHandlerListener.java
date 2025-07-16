package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.event.AsyncFailedEvent;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncFailureHandlerListener {

    private final AsyncTaskFailureRepository asyncTaskFailureRepository;

    @EventListener
    @Async("combinedExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAsyncFailed(AsyncFailedEvent event) {
        String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
        String taskName = event.getTaskName();
        String failureReason = event.getException().getMessage();

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, failureReason);
        asyncTaskFailureRepository.save(failure);
        log.error("[AsyncFailedEvent] taskName={}, requestId={}, reason={}", taskName, requestId, failureReason);

    }
}
