package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.event.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
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
public class BinaryContentStorageEventListener {

    private final BinaryContentStorage binaryContentStorage;

    private final AsyncTaskFailureRepository asyncTaskFailureRepository;

    private final BinaryContentRepository binaryContentRepository;


    @EventListener
    @Async("combinedExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void handleUpload(BinaryContentUploadEvent event) {

        binaryContentStorage.put(event.getContentId(), event.getData());

        BinaryContent binaryContent = getBinaryContent(event.getContentId());

        binaryContent.update(binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType(), BinaryContentUploadStatus.SUCCESS);

    }

    private BinaryContent getBinaryContent(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> {
                    BinaryContentNotFoundException exception = BinaryContentNotFoundException.withId(binaryContentId);
                    return exception; });
    }


}
