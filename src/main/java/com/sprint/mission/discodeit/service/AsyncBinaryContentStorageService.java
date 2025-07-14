package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.event.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncBinaryContentStorageService {

    private final BinaryContentStorage binaryContentStorage;

    private final AsyncTaskFailureRepository asyncTaskFailureRepository;

    private final BinaryContentRepository binaryContentRepository;


    @Async("combinedExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void handleUpload(BinaryContentUploadEvent event) {
        
        binaryContentStorage.put(event.getContentId(), event.getData());

        BinaryContent binaryContent = getBinaryContent(event.getContentId());

        binaryContent.update(binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType(), BinaryContentUploadStatus.SUCCESS);

    }


    @Recover
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recoverHandleUpload(Exception e, BinaryContentUploadEvent event) {
        String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
        String taskName = "handleUpload";
        String failureReason = e.getMessage();

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, failureReason);
        asyncTaskFailureRepository.save(failure);
        log.error("[Retry Failed] taskName={}, requestId={}, reason={}", taskName, requestId, failureReason);

        BinaryContent binaryContent = getBinaryContent(event.getContentId());

        binaryContent.update(binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType(), BinaryContentUploadStatus.FAILED);
    }

    private BinaryContent getBinaryContent(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> {
                    BinaryContentNotFoundException exception = BinaryContentNotFoundException.withId(binaryContentId);
                    return exception; });
    }


}
