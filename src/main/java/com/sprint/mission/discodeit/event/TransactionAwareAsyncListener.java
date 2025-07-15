package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransactionAwareAsyncListener {
  private final BinaryContentRepository binaryContentRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBinaryContentPutSuccess(BinaryContentPutSuccessEvent event) {
    binaryContentRepository.findById(event.binaryContentId()).ifPresent(binaryContent -> {
      binaryContent.setStatus(BinaryContentUploadStatus.SUCCESS);
    });
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void handleBinaryContentPutRollback(BinaryContentPutFailedEvent event) {
    binaryContentRepository.findById(event.binaryContentId()).ifPresent(binaryContent -> {
      binaryContent.setStatus(BinaryContentUploadStatus.FAILED);
    });
  }

}
